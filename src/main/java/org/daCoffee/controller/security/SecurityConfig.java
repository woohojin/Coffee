package org.daCoffee.controller.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.daCoffee.dto.ApiResponseDTO;
import org.daCoffee.jwt.JwtTokenProvider;
import org.daCoffee.service.RedisService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.config.http.SessionCreationPolicy;

import java.util.List;

@Configuration
@EnableWebSecurity
@Slf4j
@RequiredArgsConstructor
public class SecurityConfig {

  private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
  private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
  private final CustomLogoutHandler customLogoutHandler;
  private final JwtTokenProvider jwtTokenProvider;
  private final RedisService redisService;

  ObjectMapper objectMapper = new ObjectMapper();

  @Bean
  public PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();

    configuration.setAllowedOrigins(List.of(
            "http://localhost:5173", "http://localhost:5174", // React 개발 서버
            "http://3.36.83.165:5173", "http://3.36.83.165:5174", // EC2 배포 (IP, 임시)
            "https://daallcoffee.com", "https://admin.daallcoffee.com" // EC2 배포 (도메인)
    ));
    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
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
      .sessionManagement(session -> session
      .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .securityMatcher("/**")
      .authorizeHttpRequests(auth -> auth
        .requestMatchers(
          "/member/memberSignInPro",
          "/api/member/signup",
          "/api/member/checkId",
          "/api/member/me",
          "/api/member/verifyEmail",
          "/api/member/verifyCode",
          "/api/member/findAccount",
          "/api/auth/**",
          "/api/products/**",
          "/css/**",
          "/image/**",
          "/js/**",
          "/favicon.ico"
        ).permitAll() // 인증 없이 접근 가능
        .requestMatchers("/member/**").authenticated()
        .requestMatchers("/api/admin/**").hasAuthority("ROLE_ADMIN")
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
      .logout(logout -> logout
        .logoutUrl("/member/memberLogout")
        .addLogoutHandler(customLogoutHandler)
        .logoutSuccessHandler(customLogoutHandler)
      )
      .csrf(csrf -> csrf
        .ignoringRequestMatchers(
          "/api/**",
          "/member/memberSignInPro",
          "/member/memberLogout"
        )
      )
      // URLEncoder는 한글을 사용하기 위해서 UTF_8로 인코딩을 하는 것
      .exceptionHandling(ex -> ex
        .accessDeniedHandler((request, response, accessDeniedException) -> {
          response.setStatus(HttpServletResponse.SC_FORBIDDEN);
          response.setContentType("application/json;charset=UTF-8");
          ApiResponseDTO<Void> apiResponse = ApiResponseDTO.error(
                  "권한이 부족합니다.",
                  "/member/memberSignIn",
                  403
          );
          response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
        })
        .authenticationEntryPoint((request, response, authException) -> {
          response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
          response.setContentType("application/json;charset=UTF-8");
          ApiResponseDTO<Void> apiResponse = ApiResponseDTO.error(
                  "로그인이 필요합니다.",
                  "/member/memberSignIn",
                  401
          );
          response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
        })
      )
      .addFilterBefore(
        new JwtAuthenticationFilter(jwtTokenProvider, redisService),
        UsernamePasswordAuthenticationFilter.class
      )
      .addFilterAfter(
      new CspNonceFilter(),
      org.springframework.security.web.context.SecurityContextHolderFilter.class
    );

    return http.build();
  }
}
