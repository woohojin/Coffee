package org.daCoffee.service.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
  String memberSessionId; //세션에서 가져온 멤버 아이디

  private static final String SIGN_IN_URL = "/member/memberSignIn";

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    HttpSession session = request.getSession();
    memberSessionId = (String) session.getAttribute("memberId");

    if(memberSessionId == null) {
      response.sendRedirect(request.getContextPath() + SIGN_IN_URL);
      return false;
    }

    return true;
  }
}
