package com.course.prestudy.domain.user.exception;

import com.course.prestudy.core.exception.BaseException;
import org.springframework.http.HttpStatus;

public class UserException {

    /**
     * 사용자 찾을 수 없음
     */
    public static class NotFoundException extends BaseException {
        public NotFoundException() {
            super("user.not_found", HttpStatus.NOT_FOUND);
        }

        public NotFoundException(String message) {
            super("user.not_found", HttpStatus.NOT_FOUND, message);
        }
    }

    /**
     * 잘못된 정보
     * 아이디 or 비밀번호 불일치
     */
    public static class InvalidUserException extends BaseException {
        public InvalidUserException() {
            super("user.invalid", HttpStatus.UNAUTHORIZED);
        }

        public InvalidUserException(String message) {
            super("user.invalid", HttpStatus.UNAUTHORIZED, message);
        }
    }
}
