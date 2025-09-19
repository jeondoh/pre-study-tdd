package com.course.prestudy.domain.board.dto;

import com.course.prestudy.domain.board.entity.Board;

import java.time.LocalDateTime;

public record BoardViewResponse(
        Long id,
        String title,
        String author,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static BoardViewResponse from(Board board) {
        return new BoardViewResponse(
                board.getId(),
                board.getTitle(),
                board.getUser().getUsername(),
                board.getContent(),
                board.getCreatedAt(),
                board.getUpdatedAt()
        );
    }
}
