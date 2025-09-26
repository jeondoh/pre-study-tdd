package com.course.prestudy.domain.board.entity;

import com.course.prestudy.core.entity.BaseEntity;
import com.course.prestudy.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Board extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(length = 100)
    private String title;

    @Lob
    private String content;

    @Column(length = 50)
    private String password;

    public static Board makeDummyBoard(User user, String title, String content, String password) {
        return Board.builder()
                .user(user)
                .title(title)
                .content(content)
                .password(password)
                .build();
    }

    public void updateBoard(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public boolean isEqualPassword(String password) {
        return this.password.equals(password);
    }
}
