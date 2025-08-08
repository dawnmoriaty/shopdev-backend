package com.example.shopdev.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @NotBlank(message = "username must be not blank")
    private String username;
    @NotBlank(message = "password must be not blank")
    @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự")
    private String password;
}
