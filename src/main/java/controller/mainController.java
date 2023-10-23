package controller;

import service.productDAO;
import service.memberDAO;
import model.Product;
import model.Member;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.inject.Inject;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/board/")
public class mainController {

    @Inject
    private DataSource ds;

    @Autowired
    productDAO productDao;
    @Autowired
    memberDAO memberDao;

    HttpServletRequest request;
    Model m;
    HttpSession session;

    @ModelAttribute
    void init(HttpServletRequest request, Model m) {
        this.request = request;
        this.m = m;
        this.session = request.getSession();
    }

    @RequestMapping("main")
    public String main() throws Exception {

        try{
            Connection conn = (Connection) ds.getConnection();
            System.out.println("Success : " + conn);
        } catch (Exception ex) {
            System.out.println("Failed");
            ex.printStackTrace();
        }

        return "main";
    }

    @RequestMapping("product")
    public String product() throws Exception {

        String memberId = (String)session.getAttribute("memberId");
        int memberTier = 0;

        if(memberId != null && memberId.length() != 0) {
            Member mem = memberDao.memberSelectOne(memberId);
            memberTier = mem.getMemberTier();
        }

        String pageNum = request.getParameter("pageNum");
        if (pageNum == null) {
            pageNum = "1";
        }

        int pageInt = Integer.parseInt(pageNum);

        int limit = 4; // 한 page당 게시물 개수
        int bottomLine = 100; // pagination 개수

        int productCount = 0;

        if(memberTier != 0) {
            productDao.productSet();
            List<Product> list = productDao.productList(pageInt, limit, memberTier);
            productCount = productDao.productCount(memberTier);
            request.setAttribute("list", list);
        }

        int start = (pageInt - 1) / bottomLine * bottomLine + 1;
        int end = start + bottomLine - 1;
        int maxPage = (productCount / limit) + (productCount % limit == 0 ? 0 : 1);
        if (end > maxPage) {
            end = maxPage;
        }
        if (end > productCount) {
            end = productCount;
        }

        request.setAttribute("memberTier", memberTier);
        request.setAttribute("productCount", productCount);
        request.setAttribute("pageNum", pageNum);
        request.setAttribute("start", start);
        request.setAttribute("end", end);
        request.setAttribute("bottomLine", bottomLine);
        request.setAttribute("maxPage", maxPage);
        request.setAttribute("pageInt", pageInt);

        return "board/product/productBoard";
    }

    @RequestMapping("productDetail")
    public String productDetail(String productCode) throws Exception {
        Product product = productDao.productSelectOne(productCode);
        request.setAttribute("product", product);

        return "/board/product/productDetailBoard";
    }

    @RequestMapping("productSearch")
    public String productSearch() throws Exception {

        String memberId = (String)session.getAttribute("memberId");
        int memberTier = 0;

        if(memberId != null && !memberId.isEmpty()) {
            Member mem = memberDao.memberSelectOne(memberId);
            memberTier = mem.getMemberTier();
        }

        String pageNum = request.getParameter("pageNum");
        if (pageNum == null) {
            pageNum = "1";
        }

        int pageInt = Integer.parseInt(pageNum);

        int limit = 4; // 한 page당 게시물 개수

        String searchText = request.getParameter("searchText");

        int productSearchCount = 0;

        if(memberTier != 0) {
            productDao.productSet();
            List<Product> list = productDao.productSearchList(pageInt, limit, memberTier, searchText);
            productSearchCount = productDao.productSearchCount(memberTier, searchText);
            request.setAttribute("list", list);
        }

        int start = 1;
        int end = 1;
        int bottomLine = 100; // pagination 개수

        if(productSearchCount < 1) {
            start = 0;
            end = 0;
        } else if (productSearchCount > 1) {
            // 페이지 번호를 표시하는 로직
            start = (pageInt - 1) / bottomLine * bottomLine + 1;
            end = (productSearchCount / limit) + (productSearchCount % limit == 0 ? 0 : 1);
            if (end > productSearchCount) {
                end = productSearchCount;
            }
        }

        request.setAttribute("searchText", searchText);
        request.setAttribute("memberTier", memberTier);
        request.setAttribute("productSearchCount", productSearchCount);
        request.setAttribute("pageNum", pageNum);
        request.setAttribute("start", start);
        request.setAttribute("end", end);

        return "board/product/productBoard";
    }

    @RequestMapping("productBoardForm")
    public String productBoardForm() {
        return "productUploadForm";
    }

    @RequestMapping("productBoardPro")
    public String productBoardPro(Product product) throws Exception {

        String msg = "게시물 등록 실패";
        String url = "/board/product/productBoardForm";

        int num = productDao.productInsert(product);

        if (num > 0) {
            msg = "게시물을 등록하였습니다.";
            url = "/board/main";
        }

        request.setAttribute("msg", msg);
        request.setAttribute("url", url);

        return "alert";
    }

    @RequestMapping("fileUploadForm")
    public String fileUploadForm() throws Exception {
        return "board/fileUploadForm";
    }

    @RequestMapping("fileUploadPro")
    public String fileUploadPro(@RequestParam("file") MultipartFile multipartFile) throws Exception {

        String path = request.getServletContext().getRealPath("/") + "view/board/files/";
        String filename = null;

        if (!multipartFile.isEmpty()) {
            File file = new File(path, multipartFile.getOriginalFilename());
            multipartFile.transferTo(file);
            filename = multipartFile.getOriginalFilename();
            System.out.println(file);
        }

        request.setAttribute("filename", filename);
        return "board/fileUploadPro";
    }

    @RequestMapping("fileDownload")
    public void fileDownload(HttpServletResponse response) {
        try {
            String filePath = request.getServletContext().getRealPath("/") + "view/board/files/"; // 다운로드할 파일 경로
            String fileName = request.getParameter("fileName");
            filePath = filePath + fileName;

            File file = new File(filePath);

            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
            response.setContentLength((int) file.length());

            InputStream inputStream = new FileInputStream(file);
            OutputStream outputStream = response.getOutputStream();

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            inputStream.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}



