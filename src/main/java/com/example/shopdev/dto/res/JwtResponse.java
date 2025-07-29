package com.example.shopdev.dto.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class JwtResponse {
    private String accessToken;
    private final String type = "Bearer";
    private String username;
    private String email;
    private Boolean status;
    private Set<String> roles;
}
