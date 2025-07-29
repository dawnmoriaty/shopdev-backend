package com.example.shopdev.service;

import com.example.shopdev.exception.TokenRefreshException;
import com.example.shopdev.model.RefreshToken;
import com.example.shopdev.model.User;

import java.util.Optional;

public interface IRefreshTokenService {
    RefreshToken createRefreshToken(User user);
    Optional<RefreshToken> findByToken(String token);
    RefreshToken verifyExpiration(RefreshToken token) throws TokenRefreshException;
    void deleteByUser(User user);
    boolean deleteByToken(String token);
}
