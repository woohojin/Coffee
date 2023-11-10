package controller;

import model.Cart;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import service.productDAO;
import service.memberDAO;
import service.cartDAO;
import model.Product;
import model.Member;

import java.io.*;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.inject.Inject;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    @Autowired
    cartDAO cartDao;

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

        if(memberId != null && !memberId.isEmpty()) {
            Member mem = memberDao.memberSelectOne(memberId);
            memberTier = mem.getMemberTier();
        }

        String pageNum = request.getParameter("pageNum");
        if (pageNum == null) {
            pageNum = "1";
        }

        int pageInt = Integer.parseInt(pageNum);

        int limit = 16; // 한 page당 게시물 개수
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

        return "board/product/productDetailBoard";
    }

    @PostMapping("productDetailPro")
    @ResponseBody
    public Map<String, Object> productDetailPro(Cart cart) throws Exception {
        Map<String, Object> map = new HashMap<>();

        String msg = "장바구니 추가 실패";
        String memberId = (String) session.getAttribute("memberId");
        String productCode = cart.getProductCode();

        int quantity = cart.getQuantity();

        if(quantity < 1) {
            quantity = 1;
            cart.setQuantity(quantity);
        }

        Cart cartCheck = cartDao.cartSelectOne(memberId, productCode);

        if(cartCheck == null) {
            int num = cartDao.cartInsert(cart);

            if(num > 0) {
                msg = "장바구니 추가 성공";
                map.put("productCode", cart.getProductCode());
                map.put("productName", cart.getProductName());
                map.put("productUnit", cart.getProductUnit());
                map.put("quantity", cart.getQuantity());
                map.put("productPrice", cart.getProductPrice());
                map.put("productFile", cart.getProductFile());
            }
        } else {
            quantity = quantity + cartCheck.getQuantity();
            cartDao.cartQuantityUpdate(memberId, productCode, quantity);
            msg = "장바구니 추가 성공";
            map.put("productCode", cart.getProductCode());
            map.put("productName", cart.getProductName());
            map.put("productUnit", cart.getProductUnit());
            map.put("quantity", cart.getQuantity());
            map.put("productPrice", cart.getProductPrice());
            map.put("productFile", cart.getProductFile());
        }

        map.put("cartStatus", msg);
        return map;
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

    @RequestMapping("productUploadForm")
    public String productUploadForm() {
        return "board/product/productUploadForm";
    }

    @RequestMapping("productUploadPro")
    public String productUploadPro(Product product) throws Exception {

        String msg;
        String url;

        int num = productDao.productInsert(product);

        if (num > 0) {
            msg = "게시물을 등록하였습니다.";
            url = "/board/main";
        }

        msg = "게시물 등록 실패";
        url = "/board/product/productUploadForm";

        request.setAttribute("msg", msg);
        request.setAttribute("url", url);

        return "alert";
    }

    @RequestMapping("fileUploadForm")
    public String fileUploadForm() throws Exception {
        return "board/fileUploadForm";
    }

    @RequestMapping("fileUploadPro")
    public String fileUploadPro(MultipartHttpServletRequest files) throws Exception {

        String path = request.getServletContext().getRealPath("/") + "view/board/files/";
        String filename = null;

        List<MultipartFile> fileList = files.getFiles("files");

        if (fileList.size() > 0) {
            for(int i = 0; i < fileList.size(); i++) {
                filename = fileList.get(i).getOriginalFilename();
                File file = new File(path, filename);
                try {
                    fileList.get(i).transferTo(file);
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("Failed to upload file");
            System.out.println(fileList.size() + " / "  + fileList);
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



