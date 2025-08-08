package com.example.shopdev.service;

import com.example.shopdev.dto.req.ChangePasswordRequest;
import com.example.shopdev.dto.req.UpdateProfileRequest;

public interface IAccountService {
    void changePassword(ChangePasswordRequest request);
    UpdateProfileRequest updateProfile(UpdateProfileRequest request);
}
