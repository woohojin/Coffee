package org.daCoffee.controller.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.daCoffee.dto.ApiResponseDTO;
import org.daCoffee.entity.Member;
import org.daCoffee.jwt.JwtTokenProvider;
import org.daCoffee.service.MemberService;
import org.daCoffee.service.RedisService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
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
  private final JwtTokenProvider jwtTokenProvider;
  private final RedisService redisService;

  @Value("${JWT_REFRESH_EXPIRATION}")
  private long refreshExpiration;

  @Value("${cookie.domain:}")
  private String cookieDomain;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
    String username = authentication.getName();

    Member member = memberService.findById(username)
            .orElseThrow(() -> new UsernameNotFoundException("not_found"));

    String accessToken = jwtTokenProvider.generateAccessToken(
            member.getMemberId(),
            member.getMemberTier().getCode()
    );

    String refreshToken = jwtTokenProvider.generateRefreshToken(member.getMemberId());

    redisService.saveRefreshToken(member.getMemberId(), refreshToken, refreshExpiration);
    redisService.deleteBlacklist(member.getMemberId());

    ResponseCookie.ResponseCookieBuilder accessBuilder = ResponseCookie.from("accessToken", accessToken)
      .httpOnly(true)
      .sameSite("Lax")
      .path("/")
      .maxAge(1800); // 30분
    if (!cookieDomain.isBlank()) accessBuilder.domain(cookieDomain);
    ResponseCookie accessCookie = accessBuilder.build();

    ResponseCookie.ResponseCookieBuilder refreshBuilder = ResponseCookie.from("refreshToken", refreshToken)
      .httpOnly(true)
      .sameSite("Lax")
      .path("/api/auth/refresh")
      .maxAge(604800); // 7일
    if (!cookieDomain.isBlank()) refreshBuilder.domain(cookieDomain);
    ResponseCookie refreshCookie = refreshBuilder.build();

    response.addHeader("Set-Cookie", accessCookie.toString());
    response.addHeader("Set-Cookie", refreshCookie.toString());

    response.setStatus(HttpServletResponse.SC_OK);
    response.setContentType("application/json;charset=UTF-8");

    ApiResponseDTO<Void> apiResponse = ApiResponseDTO.success(null);
    response.getWriter().write(new ObjectMapper().writeValueAsString(apiResponse));

    log.info("로그인 성공 - memberId: {}, memberTier: {}", member.getMemberId(), member.getMemberTier());
  }
}
