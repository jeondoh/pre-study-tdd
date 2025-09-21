package com.course.prestudy.domain.user.exception;

import com.course.prestudy.core.exception.BaseException;
import org.springframework.http.HttpStatus;

public class UserException {

    public static class NotFoundException extends BaseException {
        public NotFoundException() {
            super("user.not_found", HttpStatus.NOT_FOUND);
        }

        public NotFoundException(String message) {
            super("user.not_found", HttpStatus.NOT_FOUND, message);
        }
    }
}
