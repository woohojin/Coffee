package org.daCoffee.config;

import org.daCoffee.service.interceptor.MemberInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
  private final MemberInterceptor memberInterceptor;

  @Autowired
  public WebConfig(MemberInterceptor memberInterceptor) {
    this.memberInterceptor = memberInterceptor;
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(memberInterceptor)
      .addPathPatterns("/**");
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/css/**")
      .addResourceLocations("classpath:/public/css/");
    registry.addResourceHandler("/image/**")
      .addResourceLocations("classpath:/public/image/");
    registry.addResourceHandler("/files/**")
      .addResourceLocations("classpath:/public/files/");
    registry.addResourceHandler("/js/**")
      .addResourceLocations("classpath:/public/js/");
  }
}