package controller;

import service.memberDAO;
import model.member;

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
@RequestMapping("/member/")
public class memberController {

    @Inject
    private DataSource ds;

    @Autowired
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

    @RequestMapping("memberLogin")
    public String memberLogin() throws Exception {
       return "/member/memberLoginForm";
    }

    @RequestMapping("memberLoginPro")
    public String memberLoginPro(String memberId, String memberPassword) throws Exception {

        String msg = "유효하지 않은 회원입니다.";
        String url = "/member/signIn";

        member mem = memberDao.memberCheckOne(memberId);

        if(mem != null) {
           int memberTier = mem.getMemberTier();
            if (memberPassword.equals(mem.getMemberPassword())) {
                session.setAttribute("memberId", memberId);
                session.setAttribute("memberTier", memberTier);

                msg = mem.getMemberId() + "님이 로그인 하였습니다.";
                url = "/board/main";
            } else {
                msg = "비밀번호가 틀립니다.";
            }
        } else {
            msg = "유효하지 않은 회원입니다.";
        }

        request.setAttribute("msg", msg);
        request.setAttribute("url", url);

        return "anchor";
    }

}

