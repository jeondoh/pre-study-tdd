package com.course.prestudy.domain.user.entity;

import com.course.prestudy.core.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "users")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    public static User makeDummyUser(String username, String password) {
        return User.builder()
                .username(username)
                .password(password)
                .build();
    }
}
