package com.course.prestudy.domain.user.exception;

import com.course.prestudy.core.exception.BaseException;
import org.springframework.http.HttpStatus;

public class AuthException {

    public static class DuplicateUsernameException extends BaseException {
        public DuplicateUsernameException() {
            super("auth.duplicate_username", HttpStatus.NOT_FOUND);
        }

        public DuplicateUsernameException(String message) {
            super("auth.duplicate_username", HttpStatus.CONFLICT, message);
        }
    }
}
