package org.daCoffee.controller.security;

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
public class SecurityConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityConfig.class);

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
          .securityMatcher("/**")
          .authorizeHttpRequests(auth -> auth
            .requestMatchers("/member/**", "/board/**", "/css/**", "/image/**", "/js/**", "/favicon.ico", "/error", "/META-INF/resources/WEB-INF/view/**").permitAll() // 인증 없이 접근 가능, error.jsp가 있는건 sitemesh가 error.jsp를 필터링 하기 때문
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
            .ignoringRequestMatchers("/error")
          )
          .exceptionHandling(ex -> ex
            .accessDeniedHandler((request, response, accessDeniedException) -> {
              LOGGER.info("Access Denied: {}", accessDeniedException.getMessage());
              request.getSession().setAttribute("errorMessage", "Access Denied: " + accessDeniedException.getMessage());
              request.getSession().setAttribute("errorStatus", 403);
              response.sendRedirect("/error");
            })
            .authenticationEntryPoint((request, response, authException) -> {
              LOGGER.info("Authentication Failed: {}", authException.getMessage());
              request.getSession().setAttribute("errorMessage", "Authentication Failed: " + authException.getMessage());
              request.getSession().setAttribute("errorStatus", 401);
              response.sendRedirect("/error");
            })
          );
        return http.build();
    }
}
