package com.example.shopdev.security.jwt;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
@RequiredArgsConstructor
public class JwtCache {
    private final JwtProvider jwtProvider;
    private final HttpServletRequest request;
    private Claims cachedClaims;
    private String cachedToken;

    public Claims getClaims() {
        if (cachedClaims == null) {
            String token = getToken();
            if (token != null && jwtProvider.validateToken(token)) {
                cachedClaims = jwtProvider.extractAllClaims(token);
            } else {
                throw new RuntimeException("Token is invalid or expired. Please login again.");
            }
        }
        return cachedClaims;
    }

    public String getToken() {
        if (cachedToken == null) {
            String bearerToken = request.getHeader("Authorization");
            if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
                cachedToken = bearerToken.substring(7);
            }
        }
        return cachedToken;
    }

    public Long getCurrentUserId() {
        Claims claims = getClaims();
        return claims.get("userId", Long.class);
    }

    // sau nay can thi dung
    public String getCurrentUsername() {
        Claims claims = getClaims();
        return claims.getSubject();
    }

    public String getCurrentUserEmail() {
        Claims claims = getClaims();
        return claims.get("email", String.class);
    }

}
