package com.example.shopdev.exception;

import org.springframework.http.HttpStatus;

public class InvalidPasswordException extends ApiException {
    public InvalidPasswordException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
