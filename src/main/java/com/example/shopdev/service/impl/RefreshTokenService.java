package com.example.shopdev.service.impl;

import com.example.shopdev.exception.TokenRefreshException;
import com.example.shopdev.model.RefreshToken;
import com.example.shopdev.model.User;
import com.example.shopdev.repository.IRefreshTokenRepository;
import com.example.shopdev.service.IRefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenService implements IRefreshTokenService {
    private final IRefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh-token.expiration}")
    private long refreshTokenDurationMs;

    @Override
    @Transactional
    public RefreshToken createRefreshToken(User user) {
        // Xóa refresh token cũ nếu có bằng cách sử dụng deleteByUser để đảm bảo xóa hoàn toàn
        refreshTokenRepository.deleteByUser(user);

        // Flush để đảm bảo deletion được thực hiện trước khi insert
        refreshTokenRepository.flush();

        // Tạo refresh token mới
        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(refreshTokenDurationMs))
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) throws TokenRefreshException {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException("Refresh token đã hết hạn. Vui lòng đăng nhập lại");
        }
        return token;
    }

    @Override
    @Transactional
    public void deleteByUser(User user) {
        refreshTokenRepository.deleteByUser(user);
    }

    @Override
    @Transactional
    public boolean deleteByToken(String token) {
        Optional<RefreshToken> tokenOptional = refreshTokenRepository.findByToken(token);

        if (tokenOptional.isEmpty()) {
            log.warn("Refresh token not found in database: {}", token);
            return false; // Token không tồn tại
        }

        refreshTokenRepository.delete(tokenOptional.get());
        log.info("Refresh token deleted successfully");
        return true;
    }
}
