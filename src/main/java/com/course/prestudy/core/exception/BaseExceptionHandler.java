package com.course.prestudy.core.exception;

import com.course.prestudy.core.config.ResponseApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class BaseExceptionHandler {

    private final MessageSource messageSource;

    /**
     * 커스텀 예외 처리
     */
    @ExceptionHandler(BaseException.class)
    public ResponseApi<Object> handleBaseException(BaseException ex) {
        String message = ex.getMessage();
        String messageKey = ex.getMessageKey();
        HttpStatusCode statusCode = ex.getStatusCode();
        String errorCode = messageSource.getMessage(messageKey + ".code", null, Locale.getDefault());
        String errorMessage = messageSource.getMessage(messageKey + ".message", null, Locale.getDefault());

        return ResponseApi.nok(statusCode, errorCode, errorMessage + " " + message);
    }

    /**
     * 커스텀 예외를 제외한 모든 에러 처리
     */
    @ExceptionHandler(Exception.class)
    public ResponseApi<Object> handleAllExceptions(Exception ex) {
        String errorCode = messageSource.getMessage("server.error.code", null, Locale.getDefault());
        String errorMessage = messageSource.getMessage("server.error.message", null, Locale.getDefault());

        log.error(ex.getMessage());

        return ResponseApi.nok(HttpStatus.INTERNAL_SERVER_ERROR, errorCode, errorMessage);
    }
}
