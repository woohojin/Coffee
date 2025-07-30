package org.daCoffee.controller.security;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
          .securityMatcher("/**")
          .authorizeHttpRequests(auth -> auth
            .requestMatchers(
              "/member/memberSignIn",
              "/member/memberSignUp",
              "/member/memberSignInPro",
              "/member/memberSignUpPro",
              "/member/memberTerms",
              "/board/**",
              "/css/**",
              "/image/**",
              "/js/**",
              "/favicon.ico",
              "/error",
              "/alert",
              "/META-INF/resources/WEB-INF/view/**"
            ).permitAll() // 인증 없이 접근 가능
            .requestMatchers("/admin/**").hasRole("ADMIN")
            .requestMatchers("/member/**").authenticated()
            .anyRequest().authenticated()
          )
          .logout(logout -> logout
            .logoutRequestMatcher(new AntPathRequestMatcher("/member/memberLogout"))
            .logoutUrl("/member/memberLogout")
            .deleteCookies("JSESSIONID", "memberId", "token")
            .clearAuthentication(true)
            .invalidateHttpSession(true)
          )
          .csrf(csrf -> csrf
            .ignoringRequestMatchers("/alert")
          )
          // URLEncoder는 한글을 사용하기 위해서 UTF_8로 인코딩을 하는 것
          .exceptionHandling(ex -> ex
            .accessDeniedHandler((request, response, accessDeniedException) -> {
              log.info("Access Denied: {}", accessDeniedException.getMessage());
              String msg = URLEncoder.encode("권한이 부족합니다.", StandardCharsets.UTF_8);
              String url = URLEncoder.encode("/member/memberSignIn", StandardCharsets.UTF_8);
              response.sendRedirect("/alert?msg=" + msg + "&url=" + url);
            })
            .authenticationEntryPoint((request, response, authException) -> {
              log.info("Authentication Failed: {}", authException.getMessage());
              String msg = URLEncoder.encode("로그인이 필요합니다.", StandardCharsets.UTF_8);
              String url = URLEncoder.encode("/member/memberSignIn", StandardCharsets.UTF_8);
              response.sendRedirect("/alert?msg=" + msg + "&url=" + url);
            })
          );
        return http.build();
    }
}
