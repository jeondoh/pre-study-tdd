package com.course.prestudy.domain.board.dto;

import com.course.prestudy.domain.board.entity.Board;

import java.time.LocalDateTime;

public record BoardCreateResponse(
        Long id,
        String title,
        String author,
        String content,
        LocalDateTime createdAt
) {
    public static BoardCreateResponse from(Board board) {
        return new BoardCreateResponse(
                board.getId(),
                board.getTitle(),
                board.getUser().getUsername(),
                board.getContent(),
                board.getCreatedAt()
        );
    }

}
