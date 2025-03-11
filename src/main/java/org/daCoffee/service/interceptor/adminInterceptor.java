package org.daCoffee.service.interceptor;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;

public class adminInterceptor implements HandlerInterceptor {

  //Context 생성
  ConfigurableApplicationContext context = new GenericXmlApplicationContext();
  //Environment 생성
  ConfigurableEnvironment environment = context.getEnvironment();
  //PropertySource 다 가져오기
  MutablePropertySources propertySources = environment.getPropertySources();

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
    throws Exception {
    HttpSession session = request.getSession();

    propertySources.addLast(new ResourcePropertySource("classpath:application.properties"));
    String[] ADMIN_ID = environment.getProperty("ADMIN_ID").split(",");

    String memberId = (String) session.getAttribute("memberId");

    if (memberId == null || !Arrays.asList(ADMIN_ID).contains(memberId)) { // 멤버 아이디가 없거나 관리자 아이디가 아닌 경우
      response.sendRedirect(request.getContextPath() + "/member/memberSignIn");
      return false;
    } else if (Arrays.asList(ADMIN_ID).contains(memberId)) {
      return true;
    }

    return false;
  }

}
