package org.daCoffee.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;

    private static final String BLACKLIST_PREFIX = "blacklist:";
    private static final String REFRESH_PREFIX = "refresh:";
    private static final String PAYMENTS_PREFIX = "payments:";
    private static final String VERIFY_PREFIX = "verify:";

    // ============= member =============

    // Refresh Token 저장
    public void saveRefreshToken(String memberId, String refreshToken, long expiration) {
        redisTemplate.opsForValue()
                .set(REFRESH_PREFIX + memberId, refreshToken, expiration, TimeUnit.MILLISECONDS);
    }

    // Refresh Token 조회
    public String getRefreshToken(String memberId) {
        return redisTemplate.opsForValue().get(REFRESH_PREFIX + memberId);
    }

    // Refresh Token 삭제 (로그아웃 시)
    public void deleteRefreshToken(String memberId) {
        redisTemplate.delete(REFRESH_PREFIX + memberId);
    }

    // 블랙리스트 등록 (memberTier 변경 시)
    public void addBlacklist(String memberId, long remainExpiration) {
        redisTemplate.opsForValue()
                .set(BLACKLIST_PREFIX + memberId, "blacklisted", remainExpiration, TimeUnit.MILLISECONDS);
    }

    // 블랙리스트 확인
    public boolean isBlacklisted(String memberId) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(BLACKLIST_PREFIX + memberId));
    }

    // 블랙리스트 삭제 (재발급 후)
    public void deleteBlacklist(String memberId) {
        redisTemplate.delete(BLACKLIST_PREFIX + memberId);
    }

    // ============= payments =============

    // 결제 데이터 저장
    public void savePaymentsData(String memberId, String orderId,
                                 String customerKey, int totalPrice) {
        Map<String, String> data = Map.of(
                "orderId", orderId,
                "customerKey", customerKey,
                "totalPrice", String.valueOf(totalPrice)
        );
        redisTemplate.opsForHash().putAll(PAYMENTS_PREFIX + memberId, data);
        redisTemplate.expire(PAYMENTS_PREFIX + memberId, 10, TimeUnit.MINUTES);
    }

    // 결제 데이터 조회
    public Map<Object, Object> getPaymentsData(String memberId) {
        return redisTemplate.opsForHash().entries(PAYMENTS_PREFIX + memberId);
    }

    // 결제 데이터 삭제
    public void deletePaymentsData(String memberId) {
        redisTemplate.delete(PAYMENTS_PREFIX + memberId);
    }

    // ============= verify =============

    // 인증코드 저장 (TTL 3분)
    public void saveVerifyCode(String email, String code) {
        redisTemplate.opsForValue()
                .set(VERIFY_PREFIX + email, code, 3, TimeUnit.MINUTES);
    }

    // 인증코드 조회
    public String getVerifyCode(String email) {
        return redisTemplate.opsForValue().get(VERIFY_PREFIX + email);
    }

    // 인증코드 삭제
    public void deleteVerifyCode(String email) {
        redisTemplate.delete(VERIFY_PREFIX + email);
    }

    // 인증 완료 저장 (TTL 10분)
    public void saveVerified(String email) {
        redisTemplate.opsForValue()
                .set(VERIFY_PREFIX + "verified:" + email, "true", 10, TimeUnit.MINUTES);
    }

    // 인증 완료 확인
    public boolean isVerified(String email) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(VERIFY_PREFIX + "verified:" + email));
    }

    // 인증 완료 삭제
    public void deleteVerified(String email) {
        redisTemplate.delete(VERIFY_PREFIX + "verified:" + email);
    }
}