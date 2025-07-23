package org.daCoffee.config;

import org.daCoffee.service.interceptor.MemberInterceptor;
import org.daCoffee.service.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  private final LoginInterceptor loginInterceptor;

  private final MemberInterceptor memberInterceptor;

  @Autowired
  public WebConfig(org.daCoffee.service.interceptor.LoginInterceptor loginInterceptor, MemberInterceptor memberInterceptor) {
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