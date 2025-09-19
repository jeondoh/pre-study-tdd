package com.course.prestudy.domain.board.dto;

import jakarta.validation.constraints.NotBlank;

public record BoardDeleteRequest(
        @NotBlank(message = "비밀번호 필수")
        String password
) {
}
