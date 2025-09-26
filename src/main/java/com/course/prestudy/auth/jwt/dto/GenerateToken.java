package com.course.prestudy.auth.jwt.dto;

import lombok.Builder;

@Builder
public record GenerateToken(
        String accessToken,
        String refreshToken,
        Long accessTokenExpired,
        Long refreshTokenExpired
) {
}
