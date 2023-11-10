package service.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class adminInterceptor implements HandlerInterceptor {

  private static final String ADMIN_ID = "admin";

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
    throws Exception {
    HttpSession session = request.getSession();

    String memberId = (String) session.getAttribute("memberId");

    if (memberId == null || !memberId.equals(ADMIN_ID)) { // 멤버 아이디가 없거나 관리자 아이디가 아닌 경우
      response.sendRedirect(request.getContextPath() + "/access-denied");
      return false;
    } else if (memberId.equals(ADMIN_ID)) {
      return true;
    }

    return false;
  }

}
