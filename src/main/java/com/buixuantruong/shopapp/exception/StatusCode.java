package com.buixuantruong.shopapp.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum StatusCode {
    SUCCESS(1000, "SUCCESS", HttpStatus.OK),
    INVALID_CREDENTIALS(1001, "INVALID_CREDENTIALS", HttpStatus.UNAUTHORIZED),
    FILE_NOT_FOUND(1002, "FILE_NOT_FOUND", HttpStatus.NOT_FOUND),
    INVALID_DATA(1003, "INVALID_DATA", HttpStatus.BAD_REQUEST);

    private final int code;
    private final HttpStatusCode httpStatusCode;
    private String message;
    StatusCode(int code, String message, HttpStatusCode httpStatusCode) {
        this.code = code;
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }
}
