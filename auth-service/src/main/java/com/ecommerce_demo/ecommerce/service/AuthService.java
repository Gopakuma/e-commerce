package com.ecommerce_demo.ecommerce.service;

import com.ecommerce_demo.ecommerce.common.enums.Role;
import com.ecommerce_demo.ecommerce.dto.request.*;
import com.ecommerce_demo.ecommerce.dto.response.*;
import com.ecommerce_demo.ecommerce.entity.User;
import com.ecommerce_demo.ecommerce.exception.*;

import java.util.List;

public interface AuthService {
    RegisterResponse register(RegisterRequest registerRequest, Role role) throws UserAlreadyExistsException;

    LoginResponse login(LoginRequest loginRequest, Role role) throws LoginFailedException;

    RefreshTokenResponse refresh(RefreshTokenRequest refreshTokenRequest, Role role) throws RefreshTokenFailedException;

    ValidateJwtResponse validateJwtToken(String token);

    ResetPasswordResponse resetPasswordService(ResetPasswordRequest resetPasswordRequest) throws ResetPasswordFailedException;

    List<User> fetchAllUsers(Boolean enabled);

    User updateUserStatus(Long id, Boolean enabled);
}
