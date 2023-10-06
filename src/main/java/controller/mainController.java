package controller;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
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
            System.out.println("Fail");
            ex.printStackTrace();
        }

        return "main";
    }

    @RequestMapping("blend")
    public String blend() throws Exception {

        return "board/blendBoard";
    }

    @RequestMapping("productBoardForm")
    public String productBoardForm() throws Exception {

        return "board/product/productBoardForm";
    }
//
//    @RequestMapping("productBoardPro")
//    public String productBoardPro() throws Exception {
//
//        String msg = "게시물 등록 실패";
//        String url = "/board/petBoardForm";
//
//        int boardType = (int) session.getAttribute("boardType");
//
//        int num = petDao.boardInsert(petBoard);
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

