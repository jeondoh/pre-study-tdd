package com.course.prestudy.domain.board.service;

import com.course.prestudy.core.dto.PagingResponse;
import com.course.prestudy.domain.board.dto.*;
import com.course.prestudy.domain.board.entity.Board;
import com.course.prestudy.domain.board.exception.BoardException;
import com.course.prestudy.domain.board.repository.BoardRepository;
import com.course.prestudy.domain.user.entity.User;
import com.course.prestudy.domain.user.exception.UserException;
import com.course.prestudy.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    // 게시글 조회
    public PagingResponse<BoardViewResponse> getBoards(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Board> boards = boardRepository.findAllWithUser(pageable);
        Page<BoardViewResponse> boardViewResponse = boards.map(BoardViewResponse::from);

        return PagingResponse.from(boardViewResponse);
    }

    // 선택 게시글 조회
    public BoardViewResponse getBoard(long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new BoardException.NotFoundException("id: " + id));

        return BoardViewResponse.from(board);
    }

    // 게시글 작성
    @Transactional
    public BoardCreateResponse createBoard(BoardCreateRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new UserException.NotFoundException("id: " + request.userId()));

        Board board = Board.builder()
                .user(user)
                .title(request.title())
                .content(request.content())
                .password(request.password())
                .build();

        boardRepository.save(board);
        return BoardCreateResponse.from(board);
    }

    // 게시글 수정
    @Transactional
    public BoardViewResponse updateBoard(long id, BoardUpdateRequest request) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new BoardException.NotFoundException("id: " + id));

        if (!board.isEqualPassword(request.password())) {
            throw new BoardException.InCorrectPassword();
        }

        board.updateBoard(request.title(), request.content());
        return BoardViewResponse.from(board);
    }

    // 게시글 삭제
    @Transactional
    public void deleteBoard(long id, BoardDeleteRequest request) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new BoardException.NotFoundException("id: " + id));

        if (!board.isEqualPassword(request.password())) {
            throw new BoardException.InCorrectPassword();
        }

        boardRepository.delete(board);
    }
}
