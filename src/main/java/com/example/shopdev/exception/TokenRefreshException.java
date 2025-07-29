package com.example.shopdev.exception;

import org.springframework.http.HttpStatus;

public class TokenRefreshException extends ApiException {
  public TokenRefreshException(String message) {
    super(message, HttpStatus.FORBIDDEN);
  }
}
