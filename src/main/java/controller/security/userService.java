package controller.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class userService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) {
        // 여기에서 실제 사용자 데이터베이스 또는 저장소에서 사용자 정보를 가져오는 작업을 수행합니다.
        // 예를 들어, 사용자 이름이 "user"이고 비밀번호가 "password"인 경우:
        return User.withUsername("user")
                .password("{noop}password") // {noop}은 암호화를 사용하지 않음을 나타냅니다.
                .roles("USER")
                .build();
    }
}