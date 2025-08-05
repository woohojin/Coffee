package org.daCoffee.service.interceptor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.daCoffee.dto.CookieDTO;
import org.daCoffee.dto.MemberDTO;
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

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class MemberInterceptor implements HandlerInterceptor {

  private final MemberDAO memberDao;
  private final CookieDAO cookieDao;
  private final CartDAO cartDao;

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
    String requestURI = request.getRequestURI();
    
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if(authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
      String memberId = authentication.getName();
      session.setAttribute("memberId", memberId);

      memberDTO = memberDao.memberSelectOne(memberId);
      if(memberDTO != null) {
        session.setAttribute("memberTier", memberDTO.getMemberTier());
      }

      if((requestURI.equals("/member/memberSignIn") || requestURI.equals("/member/memberSignUp"))) {
        String msg = URLEncoder.encode("이미 로그인하셨습니다.", StandardCharsets.UTF_8);
        String url = URLEncoder.encode("/board/main", StandardCharsets.UTF_8);
        response.sendRedirect("/alert?msg=" + msg + "&url=" + url);
        return false;
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
