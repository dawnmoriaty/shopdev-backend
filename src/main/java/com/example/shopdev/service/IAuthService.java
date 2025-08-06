package com.example.shopdev.service;

import com.example.shopdev.dto.req.LoginRequest;
import com.example.shopdev.dto.req.RegisterRequest;
import com.example.shopdev.dto.req.TokenRefreshRequest;
import com.example.shopdev.dto.res.AuthResponse;
import com.example.shopdev.exception.TokenRefreshException;
import jakarta.servlet.http.HttpServletRequest;

public interface IAuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    AuthResponse refreshToken(TokenRefreshRequest request) throws TokenRefreshException;
    void logout(String refreshToken);
}
