package com.course.prestudy.domain.user.repository;

import com.course.prestudy.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
