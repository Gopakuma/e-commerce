package com.ecommerce_demo.ecommerce.service;

import com.ecommerce_demo.ecommerce.dto.request.LoginRequest;
import com.ecommerce_demo.ecommerce.dto.request.RefreshTokenRequest;
import com.ecommerce_demo.ecommerce.dto.request.RegisterRequest;
import com.ecommerce_demo.ecommerce.dto.request.ResetPasswordRequest;
import com.ecommerce_demo.ecommerce.dto.response.*;
import com.ecommerce_demo.ecommerce.exception.LoginFailedException;
import com.ecommerce_demo.ecommerce.exception.RefreshTokenFailedException;
import com.ecommerce_demo.ecommerce.exception.ResetPasswordFailedException;
import com.ecommerce_demo.ecommerce.exception.UserAlreadyExistsException;

public interface AuthService {
    RegisterResponse register(RegisterRequest registerRequest) throws UserAlreadyExistsException;

    LoginResponse login(LoginRequest loginRequest) throws LoginFailedException;

    RefreshTokenResponse refresh(RefreshTokenRequest refreshTokenRequest) throws RefreshTokenFailedException;

    Boolean validateJwtToken();

    ResetPasswordResponse resetPasswordService(ResetPasswordRequest resetPasswordRequest) throws ResetPasswordFailedException;
}
