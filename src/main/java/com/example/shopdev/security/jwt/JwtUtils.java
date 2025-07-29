package com.example.shopdev.security.jwt;

import com.example.shopdev.security.principle.UserPrincipal;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtUtils {
    @Value("${secret_key}")
    private String jwtSecret;
    @Value("${expired}")
    private int jwtExpirationMs;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateJwtToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .claim("id", userPrincipal.getId())
                .claim("email", userPrincipal.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public String getUsernameFromJwtToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validationToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Malformed JWT token {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("Illegal JWT token {}", e.getMessage());
        } catch (JwtException e) {
            log.error("Invalid JWT token {}", e.getMessage());
        }
        return false;
    }

}