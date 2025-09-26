package com.course.prestudy.domain.user.dto;

import lombok.Builder;

@Builder
public record SignupResponse(
        Long userId,
        String username
) {
    public static SignupResponse of(Long userId, String username) {
        return SignupResponse.builder()
                .userId(userId)
                .username(username)
                .build();
    }
}
