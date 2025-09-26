package com.course.prestudy.auth.jwt.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RedisTemplate<String, String> redisTemplate;

    private static final String REFRESH_TOKEN_PREFIX = "RT:";
    @Value("${jwt.refresh-expiration}")
    private Long refreshExpiration;

    /**
     * RefreshToken 저장
     */
    public void saveRefreshToken(String username, String refreshToken) {
        String key = REFRESH_TOKEN_PREFIX + username;
        redisTemplate.opsForValue().set(
                key,
                refreshToken,
                Duration.ofSeconds(refreshExpiration)
        );

        log.info("refreshToken 저장 완료. username: {}", username);
    }
}
