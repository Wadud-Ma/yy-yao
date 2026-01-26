package com.yy.yao.model.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 业务异常
 */
@Getter
public class BusinessException extends RuntimeException {

    private final HttpStatus status;

    public BusinessException(String message) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST;
    }

    public BusinessException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.status = HttpStatus.BAD_REQUEST;
    }
}
