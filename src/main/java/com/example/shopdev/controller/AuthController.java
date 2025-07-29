package com.example.shopdev.controller;

import com.example.shopdev.dto.req.LoginRequest;
import com.example.shopdev.dto.req.RegisterRequest;
import com.example.shopdev.dto.res.ApiResponse;
import com.example.shopdev.dto.res.JwtResponse;
import com.example.shopdev.dto.res.MessageResponse;
import com.example.shopdev.service.IAuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
@Slf4j
public class AuthController {
    private final IAuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<MessageResponse>> register(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            authService.register(registerRequest);
            return ResponseEntity.ok(
                ApiResponse.<MessageResponse>builder()
                    .success(true)
                    .message("User registered successfully!")
                    .data(new MessageResponse("User registered successfully!"))
                    .build()
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                ApiResponse.<MessageResponse>builder()
                    .success(false)
                    .message(e.getMessage())
                    .data(new MessageResponse(e.getMessage()))
                    .build()
            );
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            JwtResponse jwtResponse = authService.login(loginRequest);
            return ResponseEntity.ok(
                ApiResponse.<JwtResponse>builder()
                    .success(true)
                    .message("Login successful!")
                    .data(jwtResponse)
                    .build()
            );
        } catch (Exception e) {
            log.error("Login failed for user: {}, Error: {}", loginRequest.getUsername(), e.getMessage(), e);
            return ResponseEntity.badRequest().body(
                ApiResponse.<JwtResponse>builder()
                    .success(false)
                    .message("Invalid username or password!")
                    .data(null)
                    .build()
            );
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<MessageResponse>> logout(HttpServletRequest request) {
        try {
            authService.logout(request);
            return ResponseEntity.ok(
                ApiResponse.<MessageResponse>builder()
                    .success(true)
                    .message("Logout successful!")
                    .data(new MessageResponse("User logged out successfully!"))
                    .build()
            );
        } catch (Exception e) {
            log.error("Logout failed: {}", e.getMessage());
            return ResponseEntity.badRequest().body(
                ApiResponse.<MessageResponse>builder()
                    .success(false)
                    .message("Logout failed!")
                    .data(new MessageResponse(e.getMessage()))
                    .build()
            );
        }
    }
}
