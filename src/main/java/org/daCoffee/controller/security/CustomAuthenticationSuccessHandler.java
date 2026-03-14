package org.daCoffee.controller.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.daCoffee.entity.Member;
import org.daCoffee.service.MemberService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

  private final MemberService memberService;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
    String username = authentication.getName();
    log.info("Authentication success for user : {}", username);

    Member member = memberService.findById(username)
      .orElseThrow(() -> {
        log.error("Failed to retrieve member for user: {}", username);
        return new UsernameNotFoundException("not_found");
      });

    HttpSession session = request.getSession();
    session.setAttribute("memberId", member.getMemberId());
    session.setAttribute("memberTier", member.getMemberTier());

    log.info("Session attributes set - memberId : {}, memberTier : {}", member.getMemberId(), member.getMemberTier());

    response.sendRedirect("/main");
  }
}
