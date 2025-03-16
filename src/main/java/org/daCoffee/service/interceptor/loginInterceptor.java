package org.daCoffee.service.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class loginInterceptor implements HandlerInterceptor {
  String memberSessionId; //세션에서 가져온 멤버 아이디

  private static final String SIGN_IN_URL = "/member/memberSignIn";

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    HttpSession session = request.getSession();
    memberSessionId = (String) session.getAttribute("memberId");



    if(memberSessionId == null) {
      String msg = "로그인이 되어있지 않습니다.";
      String url = "/member/memberSignIn";

      request.setAttribute("url", url);
      request.setAttribute("msg", msg);
      response.sendRedirect(request.getContextPath() + SIGN_IN_URL);

      return false;
    }

    return true;
  }
}
