package com.course.prestudy.core.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseApi<T> {

    private int statusCode;
    private String code;
    private String message;
    private T data;

    private ResponseApi(int statusCode, T data) {
        this.statusCode = statusCode;
        this.data = data;
    }

    private ResponseApi(int statusCode, String message, T data) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }

    private ResponseApi(int statusCode, String code, String message, T data) {
        this.statusCode = statusCode;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> ResponseApi<T> ok() {
        return new ResponseApi<>(HttpStatus.OK.value(), null);
    }

    public static <T> ResponseApi<T> ok(T data) {
        return new ResponseApi<>(HttpStatus.OK.value(), data);
    }
}
