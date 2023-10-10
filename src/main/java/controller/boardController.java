package controller;

import service.productDAO;
import service.memberDAO;
import model.Product;

import java.sql.Connection;
import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletContext;
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
public class boardController {

    @Inject
    private DataSource ds;

    @Autowired
    productDAO productDao;
    memberDAO memberDao;

    HttpServletRequest request;
    Model m;
    HttpSession session;
    ServletContext application;

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

        request.setAttribute("pageNum", "1");

        String pageNum = (String) request.getParameter("pageNum");
        if (pageNum == null) {
            pageNum = "1";
        }

        int pageInt = Integer.parseInt(pageNum);
        int productCount = productDao.productCount();

        int limit = 12; // 한 page당 게시물 개수
        int bottomLine = 100; // pagination 개수

        int start = (pageInt - 1) / bottomLine * bottomLine + 1;
        int end = start + bottomLine - 1;
        int maxPage = (productCount / limit) + (productCount % limit == 0 ? 0 : 1);
        if (end > maxPage) {
            end = maxPage;
        }

        int boardNum = productCount - (pageInt - 1) * limit;
        String memberId = request.getParameter("memberId");

//        int memberTier = memberDao.memberCheckTierById(memberId);
//
//        if(memberTier == 1) {
//            productDao.productSet();
//            List<product> list = productDao.productList(pageInt, limit);
//            request.setAttribute("list", list);
//        } else if(memberTier == 2) {
//            productDao.productSet();
//            List<product> list = productDao.productLeaseList(pageInt, limit);
//            request.setAttribute("list", list);
//        }

        productDao.productSet();
        List<Product> list = productDao.productList(pageInt, limit);

        request.setAttribute("list", list);
        request.setAttribute("productCount", productCount);
        request.setAttribute("boardNum", boardNum);
        request.setAttribute("start", start);
        request.setAttribute("end", end);
        request.setAttribute("bottomLine", bottomLine);
        request.setAttribute("maxPage", maxPage);
        request.setAttribute("pageInt", pageInt);

        return "board/product/productBoard";
    }

    @RequestMapping("productBoardForm")
    public String productBoardForm() throws Exception {

        return "board/product/productBoardForm";
    }

//    @RequestMapping("productBoardPro")
//    public String productBoardPro(productBoard productBOARD) throws Exception {
//
//        String msg = "게시물 등록 실패";
//        String url = "/board/petBoardForm";
//
//        int boardType = (int) session.getAttribute("boardType");
//
//        int num = productDAO.boardInsert(productBOARD);
//
//        if (num > 0) {
//            msg = "게시물을 등록하였습니다.";
//            url = "/board/petBoard?boardType=" + boardType;
//        }
//
//        request.setAttribute("msg", msg);
//        request.setAttribute("url", url);
//
//        return "index";
//    }

    @RequestMapping("imageInputForm")
    public String imageInputForm() throws Exception {
        return "board/imageInputForm";
    }

    @RequestMapping("imageInputPro")
    public String imageInputPro(@RequestParam("picture") MultipartFile multipartFile) throws Exception {

        String path = request.getServletContext().getRealPath("/") + "view/board/img/";
        String filename = null;

        if (!multipartFile.isEmpty()) {

            File file = new File(path, multipartFile.getOriginalFilename());
            multipartFile.transferTo(file);
            filename = multipartFile.getOriginalFilename();

        }

        request.setAttribute("filename", filename);
        return "board/imageInputPro";
    }

}

