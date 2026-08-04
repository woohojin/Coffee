package org.daCoffee.controller.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.daCoffee.jwt.JwtTokenProvider;
import org.daCoffee.jwt.JwtUserDetails;
import org.daCoffee.service.RedisService;
import org.slf4j.MDC;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String MEMBER_ID_KEY = "memberId";

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;

    // 쿠키에서 Access Token 추출
    private String resolveToken(HttpServletRequest request) {
        if (request.getCookies() == null) return null;

        for (Cookie cookie : request.getCookies()) {
            if ("accessToken".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = resolveToken(request);

        try {
            if (token != null && jwtTokenProvider.validateToken(token)) {
                String memberId = jwtTokenProvider.getMemberId(token);

                // 블랙리스트 확인
                if (redisService.isBlacklisted(memberId)) {
                    log.warn("블랙리스트 토큰 요청: {}", memberId);
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }

                int memberTier = jwtTokenProvider.getMemberTier(token);
                String role = memberTier == 9 ? "ROLE_ADMIN" : "ROLE_USER";

                JwtUserDetails userDetails = new JwtUserDetails(memberId, memberTier);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                List.of(new SimpleGrantedAuthority(role))
                        );

                SecurityContextHolder.getContext().setAuthentication(authentication);
                MDC.put(MEMBER_ID_KEY, memberId);
            }

            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(MEMBER_ID_KEY);
        }
    }
}