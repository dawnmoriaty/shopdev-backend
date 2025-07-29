package com.example.shopdev.controller;

import com.example.shopdev.dto.req.LoginRequest;
import com.example.shopdev.dto.req.RegisterRequest;
import com.example.shopdev.dto.req.TokenRefreshRequest;
import com.example.shopdev.dto.res.ApiResponse;
import com.example.shopdev.dto.res.AuthResponse;
import com.example.shopdev.exception.TokenRefreshException;
import com.example.shopdev.service.IAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final IAuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse authResponse = authService.register(request);

        ApiResponse<AuthResponse> response = ApiResponse.<AuthResponse>builder()
                .success(true)
                .status(HttpStatus.CREATED.value())
                .message("Đăng ký tài khoản thành công")
                .data(authResponse)
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse authResponse = authService.login(request);

        ApiResponse<AuthResponse> response = ApiResponse.<AuthResponse>builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Đăng nhập thành công")
                .data(authResponse)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(@Valid @RequestBody TokenRefreshRequest request) throws TokenRefreshException {
        AuthResponse authResponse = authService.refreshToken(request);

        ApiResponse<AuthResponse> response = ApiResponse.<AuthResponse>builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Làm mới token thành công")
                .data(authResponse)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@Valid @RequestBody TokenRefreshRequest request) {
        boolean success = authService.logout(request.getRefreshToken());

        if (!success) {
            ApiResponse<Void> response = ApiResponse.<Void>builder()
                    .success(false)
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("Đăng xuất thất bại: Token không hợp lệ")
                    .timestamp(LocalDateTime.now())
                    .build();

            return ResponseEntity.badRequest().body(response);
        }

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message("Đăng xuất thành công")
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }
}
