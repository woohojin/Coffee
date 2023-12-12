package service.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class loginInterceptor implements HandlerInterceptor {
  String memberSessionId; //세션에서 가져온 멤버 아이디

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    HttpSession session = request.getSession();
    memberSessionId = (String) session.getAttribute("memberId");

    if(memberSessionId == null) {
      response.sendRedirect(request.getContextPath() + "/member/memberSignIn");
      return false;
    }

    return true;
  }
}
