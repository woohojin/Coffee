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
            System.out.println("성공 : " + conn);
        } catch (Exception ex) {
            System.out.println("실패");
            ex.printStackTrace();
        }

        return "main";
    }

    @RequestMapping("blend")
    public String blend() throws Exception {

        return "board/blendBoard";
    }

}
