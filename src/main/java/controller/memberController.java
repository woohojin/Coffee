package controller;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import service.memberDAO;
import model.Member;

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

    @RequestMapping("memberSignUp")
    public String memberSignUp() throws Exception {
       return "member/memberSignUpForm";
    }

    @RequestMapping("memberSignUpPro")
    public String memberSignUpPro(String memberId, Member member) throws Exception {
        String msg = "이미 있는 아이디 입니다.";
        String url = "/member/memberSignUp";

        Member mem = memberDao.memberSelectOne(memberId);

        if(mem == null) {
            int num = memberDao.memberInsert(member);
            if (num > 0) {
                msg = memberId + "님의 가입이 완료되었습니다.";
                url = "/member/memberSignIn";
            } else {
                msg = "회원가입을 실패 했습니다.";
                url = "/member/memberSignUp";
            }
        }

        request.setAttribute("msg", msg);
        request.setAttribute("url", url);

        return "alert";
    }

    @RequestMapping("memberSignIn")
    public String memberSignIn() throws Exception {
        return "member/memberSignInForm";
    }

    @RequestMapping("memberSignInPro")
    public String memberSignInPro(String memberId, String memberPassword) throws Exception {

        String msg = "유효하지 않은 회원입니다.";
        String url = "/member/memberSignIn";

        Member mem = memberDao.memberSelectOne(memberId);

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

        return "alert";
    }

    @RequestMapping("memberLogout")
    public String memberLogout() throws Exception {

        String memberId = (String) session.getAttribute("memberId");
        Member mem = memberDao.memberSelectOne(memberId);

        session.invalidate();

        request.setAttribute("msg", memberId + "님이 로그아웃 되었습니다.");
        request.setAttribute("url", "/board/main");

        return "alert";
    }

}

