package com.example.shopdev.service.impl;

import com.example.shopdev.dto.req.ChangePasswordRequest;
import com.example.shopdev.dto.req.UpdateProfileRequest;
import com.example.shopdev.exception.InvalidPasswordException;
import com.example.shopdev.exception.UserNotFoundException;
import com.example.shopdev.model.User;
import com.example.shopdev.repository.IUserRepository;
import com.example.shopdev.security.jwt.JwtCache;
import com.example.shopdev.service.IAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class AccountService implements IAccountService {
    private final IUserRepository userRepository;
    private final JwtCache jwtCache;
    private final PasswordEncoder passwordEncoder;
    @Override
    public void changePassword(ChangePasswordRequest request) {
    User user = userRepository.findByUsername(jwtCache.getCurrentUsername());
    if(user==null){
        throw new UserNotFoundException("User not found");
    }
    if(!passwordEncoder.matches(request.getOldPassword(),user.getPassword())){
        throw new InvalidPasswordException("Old password is incorrect");
    }
    user.setPassword(passwordEncoder.encode(request.getNewPassword()));
    userRepository.save(user);
    }

    @Override
    public UpdateProfileRequest updateProfile(UpdateProfileRequest request) {
        User user = userRepository.findByUsername(jwtCache.getCurrentUsername());
        if(user==null){
            throw new UserNotFoundException("User not found");
        }
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        return new UpdateProfileRequest(
                user.getEmail(),
                user.getPhone()
        );
    }
}
