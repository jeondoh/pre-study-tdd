package com.course.prestudy.core.exception;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
public abstract class BaseException extends RuntimeException {

    private final String message;
    private final String messageKey;
    private final HttpStatusCode statusCode;

    public BaseException(String messageKey, HttpStatusCode statusCode) {
        this(messageKey, statusCode, null);
    }

    public BaseException(String messageKey, HttpStatusCode statusCode, String message) {
        super(messageKey);
        this.message = message;
        this.messageKey = messageKey;
        this.statusCode = statusCode;
    }
}
