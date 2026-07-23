package org.daCoffee.controller.api;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.daCoffee.dto.ApiResponseDTO;
import org.daCoffee.entity.Member;
import org.daCoffee.exception.NotFoundException;
import org.daCoffee.jwt.JwtTokenProvider;
import org.daCoffee.service.MemberService;
import org.daCoffee.service.RedisService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthApiController {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;
    private final MemberService memberService;

    @Value("${JWT_REFRESH_EXPIRATION}")
    private long refreshExpiration;

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponseDTO<Void>> refresh(HttpServletRequest request,
                                        HttpServletResponse response) {

        // 쿠키에서 Refresh Token 추출
        String refreshToken = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("refreshToken".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }

        if (refreshToken == null) {
            ApiResponseDTO<Void> body = ApiResponseDTO.error("Refresh Token이 없습니다.");
            return ResponseEntity.status(body.getStatusCode()).body(body);
        }

        // Refresh Token 유효성 검증
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            ApiResponseDTO<Void> body = ApiResponseDTO.error("유효하지 않은 Refresh Token입니다.");
            return ResponseEntity.status(body.getStatusCode()).body(body);
        }

        String memberId = jwtTokenProvider.getMemberId(refreshToken);

        // Redis에 저장된 Refresh Token과 비교
        String savedRefreshToken = redisService.getRefreshToken(memberId);
        if (!refreshToken.equals(savedRefreshToken)) {
            ApiResponseDTO<Void> body = ApiResponseDTO.error("Refresh Token이 일치하지 않습니다.");
            return ResponseEntity.status(body.getStatusCode()).body(body);
        }

        // DB에서 최신 memberTier 조회 (변경된 경우 반영)
        Member member = memberService.findById(memberId)
                .orElseThrow(() -> new NotFoundException("회원 없음"));

        // 새 Access Token 발급
        String newAccessToken = jwtTokenProvider.generateAccessToken(
            memberId,
            member.getMemberTier()
        );

        // 새 Refresh Token 발급 (Refresh Token Rotation)
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(memberId);
        redisService.saveRefreshToken(memberId, newRefreshToken, refreshExpiration);

        // 블랙리스트 삭제
        redisService.deleteBlacklist(memberId);

        // 쿠키 갱신
        ResponseCookie accessCookie = ResponseCookie.from("accessToken", newAccessToken)
            .httpOnly(true)
            .sameSite("Lax")
            .path("/")
            .maxAge(1800)
            .build();

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", newRefreshToken)
            .httpOnly(true)
            .sameSite("Lax")
            .path("/api/auth/refresh")
            .maxAge(604800)
            .build();

        response.addHeader("Set-Cookie", accessCookie.toString());
        response.addHeader("Set-Cookie", refreshCookie.toString());

        log.info("토큰 재발급 - memberId: {}", memberId);
        return ResponseEntity.ok(ApiResponseDTO.success(null));
    }
}