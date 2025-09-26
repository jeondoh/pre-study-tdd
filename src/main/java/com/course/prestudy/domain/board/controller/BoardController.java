package com.course.prestudy.domain.board.controller;

import com.course.prestudy.core.config.ResponseApi;
import com.course.prestudy.core.dto.PagingResponse;
import com.course.prestudy.domain.board.dto.*;
import com.course.prestudy.domain.board.service.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    // 전체 게시글 목록 조회 API
    @GetMapping
    public ResponseApi<PagingResponse<BoardViewResponse>> getBoards(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PagingResponse<BoardViewResponse> boards = boardService.getBoards(page, size);

        return ResponseApi.ok(boards);
    }

    // 선택한 게시글 조회 API
    @GetMapping("/{id}")
    public ResponseApi<BoardViewResponse> getBoard(
            @PathVariable Long id
    ) {
        BoardViewResponse board = boardService.getBoard(id);

        return ResponseApi.ok(board);
    }

    // 게시글 작성 API
    @PostMapping
    public ResponseApi<BoardCreateResponse> createBoard(
            @Valid @RequestBody BoardCreateRequest request
    ) {
        BoardCreateResponse board = boardService.createBoard(request);

        return ResponseApi.ok(board);
    }

    // 선택한 게시글 수정 API
    @PutMapping("/{id}")
    public ResponseApi<BoardViewResponse> updateBoard(
            @PathVariable Long id,
            @Valid @RequestBody BoardUpdateRequest request
    ) {
        BoardViewResponse board = boardService.updateBoard(id, request);

        return ResponseApi.ok(board);
    }

    // 선택한 게시글 삭제 API
    @DeleteMapping("/{id}")
    public ResponseApi<Void> deleteBoard(
            @PathVariable Long id,
            @Valid @RequestBody BoardDeleteRequest request
    ) {
        boardService.deleteBoard(id, request);

        return ResponseApi.ok();
    }
}
