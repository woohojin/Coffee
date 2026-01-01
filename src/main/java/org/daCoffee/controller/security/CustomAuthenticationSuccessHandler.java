package org.daCoffee.controller.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.daCoffee.dao.MemberDAO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.daCoffee.dto.MemberDTO;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

  private final MemberDAO memberDAO;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
    String username = authentication.getName();
    log.info("Authentication success for user : {}", username);

    MemberDTO memberDTO = memberDAO.memberSelectOne(username);

    String memberId = memberDTO.getMemberId();
    int memberTier = memberDTO.getMemberTier();

    if(memberDTO != null) {
      HttpSession session = request.getSession();
      session.setAttribute("memberId", memberId);
      session.setAttribute("memberTier", memberTier);
      log.info("Session attributes set - memberId : {}, memberTier : {}", memberId, memberTier);
    } else {
      log.error("Failed to retrieve memberDTO for user: {}", username);
    }

    response.sendRedirect("/main");
  }
}
