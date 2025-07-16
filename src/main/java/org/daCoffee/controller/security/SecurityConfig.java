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
            .requestMatchers("/member/**", "/board/**", "/css/**", "/image/**", "/js/**", "/favicon.ico", "/error", "/alert", "/META-INF/resources/WEB-INF/view/**").permitAll() // 인증 없이 접근 가능, error.jsp가 있는건 sitemesh가 error.jsp를 필터링 하기 때문
            .requestMatchers("/admin/**").hasRole("ADMIN")
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
          .exceptionHandling(ex -> ex
            .accessDeniedHandler((request, response, accessDeniedException) -> {
              log.info("Access Denied: {}", accessDeniedException.getMessage());
              response.sendRedirect("/alert?msg=Access+Denied&url=/board/main");
            })
            .authenticationEntryPoint((request, response, authException) -> {
              log.info("Authentication Failed: {}", authException.getMessage());
              response.sendRedirect("/alert?msg=Authentication+Failed&url=/board/main");
            })
          );
        return http.build();
    }
}
