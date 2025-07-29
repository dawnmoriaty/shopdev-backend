package com.example.shopdev.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
public class ApiException extends  Exception {
    private final HttpStatus status;

    public ApiException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
