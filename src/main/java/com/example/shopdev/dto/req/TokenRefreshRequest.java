package com.example.shopdev.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TokenRefreshRequest {
    @NotBlank(message = "Refresh token không được để trống")
    private String refreshToken;
}
