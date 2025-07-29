package com.example.shopdev.dto.res;

public record MessageResponse(String message) {
    public MessageResponse(String message) {
        this.message = message;
    }
}
