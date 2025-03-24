package org.daCoffee.config;

import org.daCoffee.service.interceptor.loginInterceptor;
import org.daCoffee.service.interceptor.memberInterceptor;
import org.daCoffee.service.interceptor.adminInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  private final loginInterceptor loginInterceptor;

  private final memberInterceptor memberInterceptor;

  private final adminInterceptor adminInterceptor;

  @Autowired
  public WebConfig(org.daCoffee.service.interceptor.loginInterceptor loginInterceptor, org.daCoffee.service.interceptor.memberInterceptor memberInterceptor, org.daCoffee.service.interceptor.adminInterceptor adminInterceptor) {
    this.loginInterceptor = loginInterceptor;
    this.memberInterceptor = memberInterceptor;
    this.adminInterceptor = adminInterceptor;
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(loginInterceptor)
      .addPathPatterns("/member/memberWithdrawal",
        "/member/memberHistory",
        "/member/memberMyPage",
        "/member/memberProfile",
        "/member/memberPayments",
        "/member/memberPaymentsSuccess",
        "/member/memberPaymentsFailure");
    registry.addInterceptor(memberInterceptor)
      .addPathPatterns("/**");
    registry.addInterceptor(adminInterceptor)
      .addPathPatterns("/admin/**");
  }
}