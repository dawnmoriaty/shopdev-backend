package com.example.shopdev.service;

import com.example.shopdev.dto.req.LoginRequest;
import com.example.shopdev.dto.req.RegisterRequest;
import com.example.shopdev.dto.res.JwtResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface IAuthService {
    void register(RegisterRequest registerRequest);
    JwtResponse login(LoginRequest loginRequest);
    void logout(HttpServletRequest request);
}
