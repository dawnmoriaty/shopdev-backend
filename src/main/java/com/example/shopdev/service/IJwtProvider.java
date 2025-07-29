package com.example.shopdev.service;

import com.example.shopdev.security.principle.UserPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

public interface IJwtProvider {
    String generateAccessToken(UserPrincipal userPrincipal);
    String generateRefreshToken(UserPrincipal userPrincipal);
    String extractUsername(String token);
    boolean validateToken(String token);
    long getAccessTokenExpirationTime();
}
