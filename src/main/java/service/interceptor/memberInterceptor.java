package service.interceptor;

import model.Member;
import model.CookieDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import service.cartDAO;
import service.cookieDAO;
import service.memberDAO;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class memberInterceptor implements HandlerInterceptor {

  @Autowired
  memberDAO memberDao;
  @Autowired
  cookieDAO cookieDao;
  @Autowired
  cartDAO cartDao;

  Member member;
  CookieDTO cookieDTO;

  String cookieDBToken; // DB에서 가져온 토큰
  String cookieToken; // 쿠키에서 가져온 토큰
  String memberCookieId; // 쿠키에서 가져온 멤버 아이디
  String memberSessionId; //세션에서 가져온 멤버 아이디
  String memberId; // 쿠키를 통해 DB에서 가져온 멤버 아이디
  Integer memberTier; // 쿠키를 통해 DB에서 가져온 멤버 티어
  boolean checkValidate; // 각 저장소에서 가져온 토큰 비교

  @Override
  public boolean preHandle(HttpServletRequest request,HttpServletResponse response, Object handler) throws Exception {
    HttpSession session = request.getSession();
    Cookie[] cookies = request.getCookies();

    if(cookies != null) {
      for(Cookie cookie: cookies) {
        if(cookie.getName().equals("memberId")) {
          memberCookieId = cookie.getValue();
          member = memberDao.memberSelectOne(memberCookieId);
          if(member != null) {
            memberId = member.getMemberId();
            int isDisabled = memberDao.disabledMemberSelectOne(memberId);

            if(isDisabled < 1) {
              memberTier = member.getMemberTier();
              cookieDTO = cookieDao.cookieSelectOne(memberId);
              cookieDBToken = cookieDTO.getToken();
            } else {
              cookieDBToken = "";
              Cookie cookieId = new Cookie("memberId", null);
              Cookie cookieToken = new Cookie("token", null);
              cookieId.setMaxAge(0); // 0초 = 쿠키 삭제
              cookieId.setPath("/");
              cookieToken.setMaxAge(0);
              cookieToken.setPath("/");
              response.addCookie(cookieId);
              response.addCookie(cookieToken);
              cookieDao.cookieDelete(memberId);
            }
          }
        }
        if(cookie.getName().equals("token")) {
          cookieToken = cookie.getValue();
          checkValidate = cookieDBToken.equals(cookieToken);
          if(checkValidate) {
            session.setAttribute("memberId", memberId);
            session.setAttribute("memberTier", memberTier);
          }
        }
      }
    }

    return true;
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    HttpSession session = request.getSession();

    memberSessionId = (String) session.getAttribute("memberId");

    int count = cartDao.cartCount(memberSessionId);

    session.setAttribute("cartCount", count);
  }

}
