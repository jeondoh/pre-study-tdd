package com.course.prestudy.domain.user.dto;

public record LoginRequest(
        String username,
        String password
) {
    public static LoginRequest of(String username, String password) {
        return new LoginRequest(username, password);
    }
}
