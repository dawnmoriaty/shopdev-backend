package com.example.shopdev.service;

import com.example.shopdev.security.principle.UserPrincipal;
import io.jsonwebtoken.Claims;


public interface IJwtProvider {
    String generateAccessToken(UserPrincipal userPrincipal);
    String generateRefreshToken(UserPrincipal userPrincipal);
    String extractUsername(String token);
    boolean validateToken(String token);
    long getAccessTokenExpirationTime();
    Claims extractAllClaims(String token);
}
