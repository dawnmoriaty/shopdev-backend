package com.example.shopdev.service.impl;
import com.example.shopdev.dto.req.LoginRequest;
import com.example.shopdev.dto.req.RegisterRequest;
import com.example.shopdev.dto.req.TokenRefreshRequest;
import com.example.shopdev.dto.res.AuthResponse;
import com.example.shopdev.exception.ResourceNotFoundException;
import com.example.shopdev.exception.TokenRefreshException;
import com.example.shopdev.model.RefreshToken;
import com.example.shopdev.model.Role;
import com.example.shopdev.model.User;
import com.example.shopdev.repository.IRoleRepository;
import com.example.shopdev.repository.IUserRepository;
import com.example.shopdev.security.jwt.JwtProvider;
import com.example.shopdev.security.principle.UserPrincipal;
import com.example.shopdev.service.IAuthService;
import com.example.shopdev.service.IRefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService implements IAuthService {
    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final IRefreshTokenService refreshTokenService;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username đã được sử dụng");
        }

        // Kiểm tra email đã tồn tại chưa
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email đã được sử dụng");
        }

        // Tạo user mới
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user.setStatus(true);

        // Thiết lập role
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findRoleByRoleName(request.getRole())
                .orElseThrow(() -> new ResourceNotFoundException("Role không tồn tại"));
        roles.add(userRole);
        user.setRoles(roles);

        // Lưu user
        User savedUser = userRepository.save(user);

        // Tạo UserPrincipal
        UserPrincipal userPrincipal = UserPrincipal.createUser(savedUser);

        // Tạo token
        String accessToken = jwtProvider.generateAccessToken(userPrincipal);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(savedUser);

        // Tạo response
        return buildAuthResponse(userPrincipal, accessToken, refreshToken.getToken());
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        String accessToken = jwtProvider.generateAccessToken(userPrincipal);

        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        return buildAuthResponse(userPrincipal, accessToken, refreshToken.getToken());
    }

    @Override
    public AuthResponse refreshToken(TokenRefreshRequest request) throws TokenRefreshException {
        String requestRefreshToken = request.getRefreshToken();

        // Tìm token trong database
        Optional<RefreshToken> tokenOptional = refreshTokenService.findByToken(requestRefreshToken);
        if (tokenOptional.isEmpty()) {
            throw new TokenRefreshException("Refresh token không tồn tại");
        }

        // Kiểm tra token có hợp lệ không
        RefreshToken refreshToken = tokenOptional.get();
        refreshToken = refreshTokenService.verifyExpiration(refreshToken);

        // Lấy thông tin user
        User user = refreshToken.getUser();
        UserPrincipal userPrincipal = UserPrincipal.createUser(user);

        // Tạo access token mới
        String accessToken = jwtProvider.generateAccessToken(userPrincipal);

        // Trả về response
        return buildAuthResponse(userPrincipal, accessToken, requestRefreshToken);
    }

    @Override
    public boolean logout(String token)  {
        if (token == null || token.isBlank()) {
            return false;
        }

        return refreshTokenService.deleteByToken(token);

    }

    private AuthResponse buildAuthResponse(UserPrincipal userPrincipal, String accessToken, String refreshToken) {
        Set<String> roles = userPrincipal.getAuthorities().stream()
                .map(authority -> ((SimpleGrantedAuthority) authority).getAuthority())
                .collect(Collectors.toSet());

        return AuthResponse.builder()
                .id(userPrincipal.getId())
                .username(userPrincipal.getUsername())
                .email(userPrincipal.getEmail())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(jwtProvider.getAccessTokenExpirationTime())
                .role(roles)
                .build();
    }
}
