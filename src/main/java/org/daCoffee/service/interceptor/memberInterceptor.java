package org.daCoffee.service.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.daCoffee.dto.CookieDTO;
import org.daCoffee.dto.MemberDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.daCoffee.dao.CartDAO;
import org.daCoffee.dao.CookieDAO;
import org.daCoffee.dao.MemberDAO;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class memberInterceptor implements HandlerInterceptor {

  private final MemberDAO memberDao;
  private final CookieDAO cookieDao;
  private final CartDAO cartDao;

  @Autowired
  public memberInterceptor(MemberDAO memberDao, CookieDAO cookieDao, CartDAO cartDao) {
    this.memberDao = memberDao;
    this.cookieDao = cookieDao;
    this.cartDao = cartDao;
  }

  MemberDTO memberDTO;
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
    jakarta.servlet.http.Cookie[] cookies = request.getCookies();
    String requestURI = request.getRequestURI();
    
    if(cookies != null) {
      for(jakarta.servlet.http.Cookie cookie: cookies) {
        if(cookie.getName().equals("memberId")) {
          memberCookieId = cookie.getValue();
          memberDTO = memberDao.memberSelectOne(memberCookieId);
          if(memberDTO != null) {
            memberId = memberDTO.getMemberId();
            int isDisabled = memberDao.disabledMemberSelectOne(memberId);

            if(isDisabled < 1) {
              memberTier = memberDTO.getMemberTier();
              this.cookieDTO = cookieDao.cookieSelectOne(memberId);
              cookieDBToken = this.cookieDTO.getToken();
            } else {
              cookieDBToken = "";
              jakarta.servlet.http.Cookie cookieId = new jakarta.servlet.http.Cookie("memberId", null);
              jakarta.servlet.http.Cookie cookieToken = new jakarta.servlet.http.Cookie("token", null);
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

            List<SimpleGrantedAuthority> authorities = memberTier == 9 ?
              Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")) :
              Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
            Authentication auth = new UsernamePasswordAuthenticationToken(memberId, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(auth);

            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(auth);
            session.setAttribute("SPRING_SECURITY_CONTEXT", context);
          }
        }
      }
    }

    if(session.getAttribute("memberId") != null
      && (requestURI.equals("/member/memberSignIn") || requestURI.equals("/member/memberSignUp"))) {
      response.sendRedirect("/board/main");
      return false;
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
