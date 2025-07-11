package org.daCoffee.service.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.Arrays;

@Component
@Slf4j
public class adminInterceptor implements HandlerInterceptor {

  //Spring이 자동으로 ","로 split해서 배열로 주입됨
  @Value("${SECRET_ADMIN_ID}")
  private String[] ADMIN_ID;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
    throws Exception {
    HttpSession session = request.getSession();

    String memberId = (String) session.getAttribute("memberId");

    if (memberId == null || !Arrays.asList(ADMIN_ID).contains(memberId)) { // 멤버 아이디가 없거나 관리자 아이디가 아닌 경우
      response.sendRedirect(request.getContextPath() + "/member/memberSignIn");
      return false;
    }
    return true;
  }
}
