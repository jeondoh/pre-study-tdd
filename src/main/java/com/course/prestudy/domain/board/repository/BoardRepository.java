package com.course.prestudy.domain.board.repository;

import com.course.prestudy.domain.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    // 페이징처리된 전체 게시글 조회
    @Query("SELECT b FROM Board b JOIN FETCH b.user ORDER BY b.createdAt DESC")
    Page<Board> findAllWithUser(Pageable pageable);
}
