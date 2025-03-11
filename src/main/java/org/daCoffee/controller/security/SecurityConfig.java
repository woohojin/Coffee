package org.daCoffee.controller.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final DataSource dataSource;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public SecurityConfig(UserDetailsService userDetailsService, DataSource dataSource) {
        this.userDetailsService = userDetailsService;
        this.dataSource = dataSource;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/member/**", "/board/**", "/css/**", "/image/**", "/js/**", "/favicon.ico").permitAll() // 인증 없이 접근 가능
                .anyRequest().authenticated() // 그 외 모든 요청은 인증 필요
                .and()
            .formLogin()
                .loginPage("/member/memberSignIn") // 로그인 페이지 URL 설정
                .and()
            .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/member/memberLogout"))
                .logoutUrl("/member/memberLogout") // 로그아웃 URL 설정
                .deleteCookies("JSESSIONID", "memberId", "token")
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .and()
                .csrf();
    }
}
