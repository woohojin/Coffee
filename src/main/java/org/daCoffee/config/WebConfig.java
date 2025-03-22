package org.daCoffee.config;

import org.daCoffee.service.interceptor.loginInterceptor;
import org.daCoffee.service.interceptor.memberInterceptor;
import org.daCoffee.service.interceptor.adminInterceptor;
import org.sitemesh.builder.SiteMeshFilterBuilder;
import org.sitemesh.config.ConfigurableSiteMeshFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

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


  @Bean
  public InternalResourceViewResolver viewResolver() {
    InternalResourceViewResolver resolver = new InternalResourceViewResolver();
    resolver.setPrefix("/WEB-INF/view/");
    resolver.setSuffix(".jsp");
    resolver.setViewClass(org.springframework.web.servlet.view.JstlView.class); // JSTL 지원 명시
    return resolver;
  }

  @Bean
  public FilterRegistrationBean<ConfigurableSiteMeshFilter> siteMeshFilter() {
    FilterRegistrationBean<ConfigurableSiteMeshFilter> filter = new FilterRegistrationBean<>();

    ConfigurableSiteMeshFilter siteMeshFilter = new ConfigurableSiteMeshFilter() {
      @Override
      protected void applyCustomConfiguration(SiteMeshFilterBuilder builder) {
        // sitemesh path
        builder.addDecoratorPath("/da_Coffee/*", "/WEB-INF/view/layout/layout.jsp")
          .addDecoratorPath("/board/*", "/WEB-INF/view/layout/layout.jsp")
          .addDecoratorPath("/member/*", "/WEB-INF/view/layout/layout.jsp")
          .addDecoratorPath("/admin/*", "/WEB-INF/view/layout/adminLayout.jsp")
          .addExcludedPath("*/fileSystem");
      }
    };

    filter.setFilter(siteMeshFilter);
    filter.addUrlPatterns("/*");
    return filter;
  }

  @Override
  public void addViewControllers(ViewControllerRegistry registry) {
    registry.addViewController("/").setViewName("index");
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