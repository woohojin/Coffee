package service.interceptor;

import model.Member;
import model.CookieDTO;
import org.springframework.beans.factory.annotation.Autowired;
import service.cookieDAO;
import service.memberDAO;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class cookieInterceptor implements HandlerInterceptor {

    @Autowired
    memberDAO memberDao;
    @Autowired
    cookieDAO cookieDao;

    Member member;
    CookieDTO cookieDTO;

    String cookieDBToken; // DB에서 가져온 토큰
    String cookieToken; // 쿠키에서 가져온 토큰
    String memberId; // DB에서 가져온 멤버 아이디
    String memberCookieId; // 쿠키에서 가져온 멤버 아이디
    boolean checkValidate; // 각 저장소에서 가져온 토큰 비교

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        HttpSession session = request.getSession();
        Cookie[] cookies = request.getCookies();

        if(cookies == null) {
            return true;
        }

        for(Cookie cookie: cookies) {
            if(cookie.getName().equals("memberId")) {
                memberCookieId = cookie.getValue();
                member = memberDao.memberSelectOne(memberCookieId);
                if(member != null) {
                    memberId = member.getMemberId();
                    cookieDTO = cookieDao.cookieSelectOne(memberId);
                    cookieDBToken = cookieDTO.getToken();
                }
            }
            if(cookie.getName().equals("token")) {
                cookieToken = cookie.getValue();
                checkValidate = cookieDBToken.equals(cookieToken);
                if(checkValidate == true) {
                    session.setAttribute("memberId", memberId);
                }
            }
        }

        return true;
    }

}
