package controller.security;

import model.Member;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final SqlSession sqlSession;

    @Autowired
    public CustomUserDetailsService(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = sqlSession.selectOne("member.memberSelectOne", username);

        if ("admin".equals(username)) {
            // 하드 코딩된 관리자 계정
            return User.withUsername("admin")
                    .password(member.getMemberPassword())
                    .roles("ADMIN")
                    .build();
        } else {
            return User.withUsername(member.getMemberId())
                    .password(member.getMemberPassword())
                    .disabled(false)
                    .roles("USER")
                    .build();
        }

    }
}
