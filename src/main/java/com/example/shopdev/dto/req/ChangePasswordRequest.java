package com.example.shopdev.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordRequest {
    @NotBlank(message = "password must be not blank")
    @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự")
    private String oldPassword;
    @NotBlank(message = "password must be not blank")
    @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự")
    private String newPassword;
}
