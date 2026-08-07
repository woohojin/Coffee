package org.daCoffee.controller.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.daCoffee.dto.ApiResponseDTO;
import org.daCoffee.jwt.JwtTokenProvider;
import org.daCoffee.service.RedisService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler, LogoutSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;

    @Value("${cookie.domain:}")
    private String cookieDomain;

    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication) {

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("accessToken".equals(cookie.getName())) {
                    String accessToken = cookie.getValue();
                    if (jwtTokenProvider.validateToken(accessToken)) {
                        String memberId = jwtTokenProvider.getMemberId(accessToken);

                        // Redis에서 Refresh Token 삭제
                        redisService.deleteRefreshToken(memberId);

                        // Access Token 블랙리스트 등록 ← 이게 빠져있어요
                        long remainExpiration = jwtTokenProvider.getRemainExpiration(accessToken);
                        redisService.addBlacklist(memberId, remainExpiration);

                        log.info("로그아웃 - memberId: {}", memberId);
                    }
                }
            }
        }

        // Access Token 쿠키 삭제
        ResponseCookie.ResponseCookieBuilder accessBuilder = ResponseCookie.from("accessToken", "")
                .httpOnly(true)
                .sameSite("Lax")
                .path("/")
                .maxAge(0);
        if (!cookieDomain.isBlank()) accessBuilder.domain(cookieDomain);
        ResponseCookie accessCookie = accessBuilder.build();

        // Refresh Token 쿠키 삭제
        ResponseCookie.ResponseCookieBuilder refreshBuilder = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .sameSite("Lax")
                .path("/api/auth/refresh")
                .maxAge(0);
        if (!cookieDomain.isBlank()) refreshBuilder.domain(cookieDomain);
        ResponseCookie refreshCookie = refreshBuilder.build();

        response.addHeader("Set-Cookie", accessCookie.toString());
        response.addHeader("Set-Cookie", refreshCookie.toString());
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request,
                                HttpServletResponse response,
                                Authentication authentication) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(
                new ObjectMapper().writeValueAsString(ApiResponseDTO.success(null))
        );
    }
}