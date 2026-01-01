package org.daCoffee.controller.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

import javax.sql.DataSource;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Configuration
@EnableWebSecurity
@Slf4j
@RequiredArgsConstructor
public class SecurityConfig {

  private final DataSource dataSource;
  private final UserDetailsService userDetailsService;
  private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

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
            "/css/**",
            "/image/**",
            "/js/**",
            "/favicon.ico",
            "/error",
            "/alert",
            "/META-INF/resources/WEB-INF/view/**",
            "/test/**"
          ).permitAll() // 인증 없이 접근 가능
          .requestMatchers("/admin/**").hasRole("ADMIN")
          .requestMatchers("/member/**").authenticated()
          .anyRequest().authenticated()
        )
        .formLogin(form -> form
          .loginPage("/member/memberSignIn")
          .loginProcessingUrl("/member/memberSignInPro")
          .defaultSuccessUrl("/main", true)
          .successHandler(customAuthenticationSuccessHandler)
          .usernameParameter("memberId")
          .passwordParameter("memberPassword")
          .failureHandler((request, response, exception) -> {
            String errorMessage = switch (exception.getClass().getSimpleName()) {
              case "UsernameNotFoundException" -> "존재하지 않는 아이디입니다.";
              case "DisabledException" -> "비활성화된 아이디입니다.";
              default -> "아이디 또는 비밀번호가 잘못되었습니다.";
            };
            String msg = URLEncoder.encode(errorMessage, StandardCharsets.UTF_8);
            String url = URLEncoder.encode("/member/memberSignIn", StandardCharsets.UTF_8);
            response.sendRedirect("/alert?msg=" + msg + "&url=" + url);
          })
        )
        .rememberMe(remember -> remember
          .key("uniqueAndSecretKey") // 고유한 키 (환경 변수로 관리 권장)
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
        );
      return http.build();
  }
}
