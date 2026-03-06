package org.daCoffee.service.interceptor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.daCoffee.dto.MemberDTO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.daCoffee.dao.CartDAO;
import org.daCoffee.dao.MemberDAO;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
@Slf4j
@RequiredArgsConstructor
public class MemberInterceptor implements HandlerInterceptor {

  private final MemberDAO memberDao;
  private final CartDAO cartDao;

  @Override
  public boolean preHandle(HttpServletRequest request,HttpServletResponse response, Object handler) throws Exception {
    HttpSession session = request.getSession();
    String requestURI = request.getRequestURI();
    
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if(authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
      String memberId = authentication.getName();
      session.setAttribute("memberId", memberId);

      MemberDTO memberDTO = memberDao.memberSelectOne(memberId);

      if(memberDTO != null) {
        session.setAttribute("memberTier", memberDTO.getMemberTier());
      }

      if((requestURI.equals("/member/memberSignIn") || requestURI.equals("/member/memberSignUp"))) {
        String msg = URLEncoder.encode("이미 로그인하셨습니다.", StandardCharsets.UTF_8);
        String url = URLEncoder.encode("/main", StandardCharsets.UTF_8);
        response.sendRedirect("/alert?msg=" + msg + "&url=" + url);
        return false;
      }
    }

    return true;
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
    HttpSession session = request.getSession();

    String memberSessionId = (String) session.getAttribute("memberId");

    if (memberSessionId != null) {
      int count = cartDao.cartCount(memberSessionId);
      session.setAttribute("cartCount", count);
    } else {
      session.setAttribute("cartCount", 0);
      log.warn("memberSessionId is null");
    }

  }

}
