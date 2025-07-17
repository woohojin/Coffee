package org.daCoffee.config;

import org.daCoffee.service.interceptor.loginInterceptor;
import org.daCoffee.service.interceptor.memberInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  private final loginInterceptor loginInterceptor;

  private final memberInterceptor memberInterceptor;

  @Autowired
  public WebConfig(org.daCoffee.service.interceptor.loginInterceptor loginInterceptor, org.daCoffee.service.interceptor.memberInterceptor memberInterceptor) {
    this.loginInterceptor = loginInterceptor;
    this.memberInterceptor = memberInterceptor;
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
        "/member/memberPaymentsFailure",
        "/member/memberCart");
    registry.addInterceptor(memberInterceptor)
      .addPathPatterns("/**");
  }
}