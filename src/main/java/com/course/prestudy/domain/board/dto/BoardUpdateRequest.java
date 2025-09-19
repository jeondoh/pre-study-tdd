package com.course.prestudy.domain.board.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BoardUpdateRequest(
        @NotBlank(message = "제목 필수")
        @Size(max = 100, message = "제목 100자 초과 할수 없음")
        String title,

        @NotBlank(message = "내용 필수")
        String content,

        @NotBlank(message = "비밀번호 필수")
        String password
) {
}
