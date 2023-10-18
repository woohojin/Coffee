package controller;

import model.CookieDTO;
import service.cookieDAO;
import service.memberDAO;
import model.Member;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Controller
@RequestMapping("/member/")
public class memberController {

    @Autowired
    private DataSource ds;

    @Autowired
    memberDAO memberDao;

    @Autowired
    cookieDAO cookieDao;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    HttpServletRequest request;
    HttpServletResponse response;
    Model m;
    HttpSession session;

    @ModelAttribute
    void init(HttpServletRequest request, Model m, HttpServletResponse response) {
        this.request = request;
        this.m = m;
        this.session = request.getSession();
        this.response = response;
    }

    public static class SHA256 {

        public String encrypt(String text) throws NoSuchAlgorithmException {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(text.getBytes());

            return bytesToHex(md.digest());
        }

        private String bytesToHex(byte[] bytes) {
            StringBuilder builder = new StringBuilder();
            for (byte b : bytes) {
                builder.append(String.format("%02x", b));
            }
            return builder.toString();
        }

    }

    @RequestMapping("memberSignUp")
    public String memberSignUp() throws Exception {
       return "member/memberSignUpForm";
    }

    @RequestMapping("memberSignUpPro")
    public String memberSignUpPro(Member member) throws Exception {
        String msg = "이미 있는 아이디 입니다.";
        String url = "/member/memberSignUp";

        String memberId = member.getMemberId();
        Member mem = memberDao.memberSelectOne(memberId);

        if(mem == null) {
            member.setMemberPassword(passwordEncoder.encode(member.getMemberPassword()));
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
    public String memberSignInPro(String memberId, String memberPassword, String autoLogin) throws Exception {

        //Context 생성
        ConfigurableApplicationContext context = new GenericXmlApplicationContext();
        //Environment 생성
        ConfigurableEnvironment environment = context.getEnvironment();
        //PropertySource 다 가져오기
        MutablePropertySources propertySources = environment.getPropertySources();

        propertySources.addLast(new ResourcePropertySource("classpath:application.properties"));

        String keyString = environment.getProperty("CookieLogin");

        SHA256 sha256 = new SHA256();

        String msg = "존재하지 않는 회원입니다.";
        String url = "/member/memberSignIn";

        Member mem = memberDao.memberSelectOne(memberId);

        if (mem != null) {
            if (passwordEncoder.matches(memberPassword, mem.getMemberPassword())) {
                session.setAttribute("memberId", memberId);

                url = "/board/main";
                msg = memberId + "님이 로그인 하였습니다.";

                if(autoLogin != null) {
                    String encryptKey = mem.getMemberId() + keyString;

                    String token = sha256.encrypt(encryptKey);

                    int num = cookieDao.cookieInsert(memberId, token);

                    if(num > 0) {
                        Cookie cookieId = new Cookie("memberId", memberId);
                        Cookie cookieToken = new Cookie("token", token);
                        cookieId.setMaxAge(60 * 60 * 24 * 30); // 60 * 60 * 24 * 30 == 30days
                        cookieId.setPath("/");
                        cookieToken.setMaxAge(60 * 60 * 24 * 30);
                        cookieToken.setPath("/");
                        response.addCookie(cookieId);
                        response.addCookie(cookieToken);
                    }
                }
            } else {
                msg = "비밀번호가 틀립니다.";
                url = "/member/memberSignIn";
            }
        } else {
            msg = "존재하지 않는 아이디입니다.";
            url = "/member/memberSignIn";
        }

        request.setAttribute("msg", msg);
        request.setAttribute("url", url);

        return "alert";
    }

    @RequestMapping("memberLogout")
    public String memberLogout() throws Exception {
        String memberId = (String) session.getAttribute("memberId");
        session.invalidate();

        Cookie cookieId = new Cookie("memberId", null);
        Cookie cookieToken = new Cookie("token", null);
        cookieId.setMaxAge(0); // 0초 = 쿠키 삭제
        cookieId.setPath("/");
        cookieToken.setMaxAge(0);
        cookieToken.setPath("/");
        response.addCookie(cookieId);
        response.addCookie(cookieToken);

        int num = cookieDao.cookieDelete(memberId);

        String msg = "로그아웃 되었습니다.";
        String url = "/board/main";

        request.setAttribute("msg", msg);
        request.setAttribute("url", url);

        return "alert";
    }

}

