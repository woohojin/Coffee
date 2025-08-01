package org.daCoffee.controller.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.daCoffee.dao.MemberDAO;
import org.daCoffee.dto.MemberDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberDAO memberDAO;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Loading user: {}", username);

        MemberDTO memberDTO = memberDAO.memberSelectOne(username);
        try {
            if (memberDTO == null) {
                log.error("User not found: {}", username);
                throw new UsernameNotFoundException("not_found");
            }
            // 나머지 코드 동일
        } catch (Exception e) {
            log.error("Error loading user {}: {}", username, e.getMessage(), e);
            throw new UsernameNotFoundException("Error loading user", e);
        }

        int isDisabled = memberDAO.disabledMemberSelectOne(username);
        if (isDisabled > 0) {
            log.warn("User disabled: {}", username);
            throw new DisabledException("disabled");
        }

        String role = memberDTO.getMemberTier() == 9 ? "ADMIN" : "USER";
        log.info("User password: {}, role: {}", memberDTO.getMemberPassword(), role);

        if (memberDTO.getMemberPassword() == null || memberDTO.getMemberPassword().isEmpty()) {
            log.error("Password is null or empty for user: {}", username);
            throw new UsernameNotFoundException("Invalid password");
        }

        return User.withUsername(memberDTO.getMemberId())
          .password(memberDTO.getMemberPassword())
          .disabled(false)
          .roles(role)
          .build();
    }
}