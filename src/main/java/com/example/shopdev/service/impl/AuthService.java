package com.example.shopdev.service.impl;

import com.example.shopdev.constants.RoleName;
import com.example.shopdev.dto.req.LoginRequest;
import com.example.shopdev.dto.req.RegisterRequest;
import com.example.shopdev.dto.res.JwtResponse;
import com.example.shopdev.model.Role;
import com.example.shopdev.model.User;
import com.example.shopdev.repository.IRoleRepository;
import com.example.shopdev.repository.IUserRepository;
import com.example.shopdev.security.jwt.JwtUtils;
import com.example.shopdev.security.principle.UserPrincipal;
import com.example.shopdev.service.IAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService implements IAuthService {
    private final AuthenticationManager authenticationManager;
    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    @Override
    public void register(RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new IllegalArgumentException("Error: Username is already taken!");
        }

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new IllegalArgumentException("Error: Email is already in use!");
        }

        User user = User.builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .status(true)
                .build();

        Set<Role> roles = new HashSet<>();

        // Tìm hoặc tạo ROLE_USER nếu chưa có
        Role userRole = roleRepository.findRoleByRoleName(RoleName.ROLE_USER)
                .orElseGet(() -> {
                    // Nếu chưa có thì tạo mới
                    Role newRole = new Role();
                    newRole.setRoleName(RoleName.ROLE_USER);
                    return roleRepository.save(newRole);
                });

        roles.add(userRole);
        user.setRoles(roles);
        userRepository.save(user);
    }

    @Override
    public JwtResponse login(LoginRequest loginRequest) {
        // Authenticate the user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        // Set authentication in security context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate JWT token
        String jwt = jwtUtils.generateJwtToken(authentication);

        // Get user principal
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        // Extract roles
        Set<String> roles = userPrincipal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        // Return JWT response
        return new JwtResponse(
                jwt,
                userPrincipal.getUsername(),
                userPrincipal.getEmail(),
                userPrincipal.getStatus(),
                roles
        );
    }

    @Override
    public void logout(String token) {
        // Validate the token first
        if (!jwtUtils.validationToken(token)) {
            throw new RuntimeException("Invalid or expired token");
        }

        // Clear the security context
        SecurityContextHolder.clearContext();
    }
}
