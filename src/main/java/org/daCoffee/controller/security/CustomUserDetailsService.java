package org.daCoffee.controller.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.daCoffee.entity.Member;
import org.daCoffee.service.MemberService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberService memberService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Loading user: {}", username);

        Member member = memberService.findById(username)
          .orElseThrow(() -> {
              log.error("User not found: {}", username);
              return new UsernameNotFoundException("not_found");
          });

        if (memberService.isDisabled(username)) {
            log.warn("User disabled: {}", username);
            throw new DisabledException("disabled");
        }

        if (member.getMemberPassword() == null || member.getMemberPassword().isEmpty()) {
            log.error("Password is null or empty for user: {}", username);
            throw new UsernameNotFoundException("Invalid password");
        }

        String role = member.getMemberTier() == 9 ? "ADMIN" : "USER";

        return User.withUsername(member.getMemberId())
          .password(member.getMemberPassword())
          .disabled(false)
          .roles(role)
          .build();
    }
}