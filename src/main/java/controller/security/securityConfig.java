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
public class securityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    public securityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new  BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/board/**").permitAll()
                .antMatchers("/member/**").permitAll()
                .antMatchers("/admin/**").authenticated()
                .and()
            .formLogin()
                .loginPage("/member/memberSignIn")
                .permitAll()
                .defaultSuccessUrl("/board/main") // 로그인 성공 후 이동할 페이지
                .failureUrl("/member/memberSignIn") // 로그인 실패 후 이동할 페이지
                .and()
            .logout()
                .logoutSuccessUrl("/board/main")
                .permitAll()
                .and()
            .sessionManagement()
                .sessionFixation()
                .newSession()
                .maximumSessions(1) // 동시 로그인 세션 수 제한
                .expiredUrl("/board/main"); // 세션 만료 시 이동할 페이지
    }

}