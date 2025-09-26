package com.course.prestudy.domain.user.entity;

import com.course.prestudy.core.entity.BaseEntity;
import com.course.prestudy.domain.user.dto.UserRole;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "users")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Enumerated(EnumType.STRING)
    UserRole userRole;

    public static User makeDummyUser(String username, String password) {
        return User.builder()
                .username(username)
                .password(password)
                .build();
    }

    public static User createUser(String username, String password) {
        return User.builder()
                .username(username)
                .password(password)
                .userRole(UserRole.USER)
                .build();
    }
}
