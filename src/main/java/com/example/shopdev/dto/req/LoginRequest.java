package com.example.shopdev.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @NotBlank(message = "username must be not blank")
    private String username;
    @NotBlank(message = "password must be not blank")
    private String password;
}
