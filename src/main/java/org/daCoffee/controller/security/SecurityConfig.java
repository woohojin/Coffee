package org.daCoffee.controller.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.daCoffee.dto.ApiResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.sql.DataSource;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Configuration
@EnableWebSecurity
@Slf4j
@RequiredArgsConstructor
public class SecurityConfig {

  private final DataSource dataSource;
  private final UserDetailsService userDetailsService;
  private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
  private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

  ObjectMapper objectMapper = new ObjectMapper();

  @Value("${REMEMBER_ME_KEY}")
  private String rememberMeKey;

  @Bean
  public PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
  }

  @Bean
  public PersistentTokenRepository persistentTokenRepository() {
    JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
    tokenRepository.setDataSource(dataSource);
    return tokenRepository;
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();

    configuration.setAllowedOrigins(List.of("http://localhost:5173")); // React 개발 서버
    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    configuration.setAllowedHeaders(List.of("*"));
    configuration.setAllowCredentials(true); // 쿠키/세션 허용

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  @Bean
  public SecurityFilterChain strictFilterChain(HttpSecurity http) throws Exception {
    http
      .headers(headers -> headers
        .frameOptions(frame -> frame.deny())
        .xssProtection(xss -> xss.disable()))
      .cors(cors -> cors.configurationSource(corsConfigurationSource()))
      .securityMatcher("/**")
      .authorizeHttpRequests(auth -> auth
        .requestMatchers(
          "/member/memberSignIn",
          "/member/memberSignUp",
          "/member/memberSignInPro",
          "/member/memberSignUpPro",
          "/member/memberTerms",
          "/member/memberFindAccount",
          "/api/member/verifyEmail",
          "/api/member/verifyCode",
          "/api/member/findAccount",
          "/css/**",
          "/image/**",
          "/js/**",
          "/favicon.ico",
          "/error",
          "/alert",
          "/META-INF/resources/WEB-INF/view/**",
          "/test/**",
          "/main"
        ).permitAll() // 인증 없이 접근 가능
        .requestMatchers("/admin/**").hasRole("ADMIN")
        .requestMatchers("/member/**").authenticated()
        .anyRequest().authenticated()
      )
      .formLogin(form -> form
        .loginPage("/member/memberSignIn")
        .loginProcessingUrl("/member/memberSignInPro")
        .usernameParameter("memberId")
        .passwordParameter("memberPassword")
        .successHandler(customAuthenticationSuccessHandler)
        .failureHandler(customAuthenticationFailureHandler)
      )
      .rememberMe(remember -> remember
        .key(rememberMeKey)
        .tokenRepository(persistentTokenRepository())
        .tokenValiditySeconds(60 * 60 * 24 * 30) // 30일
        .userDetailsService(userDetailsService)
        .rememberMeParameter("autoLogin") // 폼의 체크박스 이름
        .rememberMeCookieName("remember-me") // 쿠키 이름
      )
      .logout(logout -> logout
        .logoutRequestMatcher(new AntPathRequestMatcher("/member/memberLogout"))
        .logoutUrl("/member/memberLogout")
        .deleteCookies("JSESSIONID", "remember-me")
        .clearAuthentication(true)
        .invalidateHttpSession(true)
        .logoutSuccessUrl("/main")
      )
      .csrf(csrf -> csrf
        .ignoringRequestMatchers("/alert",
          "/api/member/verifyEmail",
          "/api/member/verifyCode",
          "/api/member/findAccount",
          "/member/memberSignInPro",
          "/member/memberProfilePro" )
      )
      // URLEncoder는 한글을 사용하기 위해서 UTF_8로 인코딩을 하는 것
      .exceptionHandling(ex -> ex
        .accessDeniedHandler((request, response, accessDeniedException) -> {
          log.info("Access Denied: {}", accessDeniedException.getMessage());

          String uri = request.getRequestURI();
          if (uri.startsWith("/api/")) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json;charset=UTF-8");

            ApiResponseDTO<Void> apiResponse = ApiResponseDTO.error(
              "권한이 부족합니다.",
              "/member/memberSignIn",
              403
            );

            response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
          }  else {
            String msg = URLEncoder.encode("권한이 부족합니다.", StandardCharsets.UTF_8);
            String url = URLEncoder.encode("/main", StandardCharsets.UTF_8);
            response.sendRedirect("/alert?msg=" + msg + "&url=" + url);
          }
        })
        .authenticationEntryPoint((request, response, authException) -> {
          log.info("Authentication Required: {}", authException.getMessage());

          String uri = request.getRequestURI();
          if (uri.startsWith("/api/")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");

            ApiResponseDTO<Void> apiResponse = ApiResponseDTO.error(
              "로그인이 필요합니다.",
              "/member/memberSignIn",
              401
            );

            response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
          } else {
            String msg = URLEncoder.encode("로그인이 필요합니다.", StandardCharsets.UTF_8);
            String url = URLEncoder.encode("/member/memberSignIn", StandardCharsets.UTF_8);
            response.sendRedirect("/alert?msg=" + msg + "&url=" + url);
          }
        })
      );
    http.addFilterAfter(
      new CspNonceFilter(),
      org.springframework.security.web.context.SecurityContextHolderFilter.class
    );
    return http.build();
  }
}
