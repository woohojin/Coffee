package controller.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/admin/**").hasRole("ADMIN") // 어드민만 접근 가능
                .antMatchers("/member/**").permitAll() // 인증 없이 접근 가능
                .antMatchers("/board/**").permitAll()
                .anyRequest().authenticated() // 그 외 모든 요청은 인증 필요
                .and()
            .formLogin()
                .loginPage("/member/memberSignIn") // 로그인 페이지 URL 설정
                .defaultSuccessUrl("/board/main") // 로그인 성공 후 이동할 URL
                .and()
            .logout()
                .logoutUrl("/member/memberLogout") // 로그아웃 URL 설정
                .logoutSuccessUrl("/board/main"); // 로그아웃 후 이동할 URL
    }
}
