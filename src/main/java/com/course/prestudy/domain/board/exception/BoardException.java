package com.course.prestudy.domain.board.exception;

import com.course.prestudy.core.exception.BaseException;
import org.springframework.http.HttpStatus;

public class BoardException {

    public static class NotFoundException extends BaseException {
        public NotFoundException() {
            super("board.not_found", HttpStatus.NOT_FOUND);
        }

        public NotFoundException(String message) {
            super("board.not_found", HttpStatus.NOT_FOUND, message);
        }
    }

    public static class InCorrectPassword extends BaseException {
        public InCorrectPassword() {
            super("board.password_incorrect", HttpStatus.BAD_REQUEST);
        }

        public InCorrectPassword(String message) {
            super("board.password_incorrect", HttpStatus.BAD_REQUEST, message);
        }
    }

}
