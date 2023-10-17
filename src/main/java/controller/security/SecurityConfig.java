package controller.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    private final UserDetailsService userDetailsService;
    private final DataSource dataSource;
    private String rememberMeKey;

    public void setRememberMeKey(String rememberMeKey) {
        this.rememberMeKey = rememberMeKey;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public SecurityConfig(UserDetailsService userDetailsService, DataSource dataSource) {
        this.userDetailsService = userDetailsService;
        this.dataSource = dataSource;
    }

    @Bean
    public PersistentTokenRepository tokenRepository(){
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        logger.debug(dataSource.toString());
        logger.debug(tokenRepository.getDataSource().toString());
        return tokenRepository;
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
            .rememberMe()
                .key(System.getenv("REMEMBER_ME_KEY")) // key를 환경변수로 지정
                .rememberMeParameter("remember_me")
                .tokenRepository(tokenRepository())
                .tokenValiditySeconds(86400) // 토큰 유효기간 86400 = 1일
                .and()
            .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/member/memberLogout"))
                .logoutUrl("/member/memberLogout") // 로그아웃 URL 설정
                .logoutSuccessUrl("/board/main") // 로그아웃 후 이동할 URL
                .deleteCookies("JSESSIONID", "remember-me")
                .and()
                .csrf();
    }
}
