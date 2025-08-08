package com.example.shopdev.controller;

import com.example.shopdev.dto.req.ChangePasswordRequest;
import com.example.shopdev.dto.req.UpdateProfileRequest;
import com.example.shopdev.dto.res.ApiResponse;
import com.example.shopdev.service.IAccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class AccountController {
    private final IAccountService accountService;
    @PutMapping("/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(@RequestBody @Valid ChangePasswordRequest request) {
        accountService.changePassword(request);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(true)
                .status(200)
                .message("Password changed successfully")
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<ApiResponse<UpdateProfileRequest>> updateProfile(@RequestBody @Valid UpdateProfileRequest request) {
        UpdateProfileRequest result = accountService.updateProfile(request);
        ApiResponse<UpdateProfileRequest> response = ApiResponse.<UpdateProfileRequest>builder()
                .success(true)
                .status(200)
                .message("Profile updated successfully")
                .data(result)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }
}
