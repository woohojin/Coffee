package org.daCoffee.service.interceptor;

import org.daCoffee.model.Member;
import org.daCoffee.model.Cookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.daCoffee.service.CartDAO;
import org.daCoffee.service.CookieDAO;
import org.daCoffee.service.MemberDAO;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class memberInterceptor implements HandlerInterceptor {

  private static Logger LOGGER = LoggerFactory.getLogger(memberInterceptor.class);

  private final MemberDAO memberDao;
  private final CookieDAO cookieDao;
  private final CartDAO cartDao;

  @Autowired
  public memberInterceptor(MemberDAO memberDao, CookieDAO cookieDao, CartDAO cartDao) {
    this.memberDao = memberDao;
    this.cookieDao = cookieDao;
    this.cartDao = cartDao;
  }

  Member member;
  Cookie cookie;

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
    javax.servlet.http.Cookie[] cookies = request.getCookies();

    if(cookies != null) {
      for(javax.servlet.http.Cookie cookie: cookies) {
        if(cookie.getName().equals("memberId")) {
          memberCookieId = cookie.getValue();
          member = memberDao.memberSelectOne(memberCookieId);
          if(member != null) {
            memberId = member.getMemberId();
            int isDisabled = memberDao.disabledMemberSelectOne(memberId);

            if(isDisabled < 1) {
              memberTier = member.getMemberTier();
              this.cookie = cookieDao.cookieSelectOne(memberId);
              cookieDBToken = this.cookie.getToken();
            } else {
              cookieDBToken = "";
              javax.servlet.http.Cookie cookieId = new javax.servlet.http.Cookie("memberId", null);
              javax.servlet.http.Cookie cookieToken = new javax.servlet.http.Cookie("token", null);
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

    if (cartDao == null) {
      session.setAttribute("cartCount", 0);
      return;
    }

    if (memberSessionId != null) {
      int count = cartDao.cartCount(memberSessionId);
      session.setAttribute("cartCount", count);
    } else {
      session.setAttribute("cartCount", 0);
      System.out.println("memberSessionId is null");
    }

  }

}
