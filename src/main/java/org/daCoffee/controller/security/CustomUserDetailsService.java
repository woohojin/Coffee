package org.daCoffee.controller.security;

import org.daCoffee.model.Member;
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

        return User.withUsername(member.getMemberId())
                .password(member.getMemberPassword())
                .disabled(false)
                .roles("USER")
                .build();
    }
}
