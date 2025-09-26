package com.course.prestudy.domain.user.dto;

public record LoginRequest(
        String username,
        String password
) {
}
