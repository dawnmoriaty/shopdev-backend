package com.example.shopdev.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends ApiException  {
    public ForbiddenException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}
