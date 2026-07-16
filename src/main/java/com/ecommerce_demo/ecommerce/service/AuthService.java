package com.ecommerce_demo.ecommerce.service;

import com.ecommerce_demo.ecommerce.dto.request.LoginRequest;
import com.ecommerce_demo.ecommerce.dto.request.RefreshTokenRequest;
import com.ecommerce_demo.ecommerce.dto.request.RegisterRequest;
import com.ecommerce_demo.ecommerce.dto.response.LoginResponse;
import com.ecommerce_demo.ecommerce.dto.response.RefreshTokenResponse;
import com.ecommerce_demo.ecommerce.dto.response.RegisterResponse;
import com.ecommerce_demo.ecommerce.exception.LoginFailedException;
import com.ecommerce_demo.ecommerce.exception.RefreshTokenFailedException;
import com.ecommerce_demo.ecommerce.exception.UserAlreadyExistsException;

public interface AuthService {
    RegisterResponse register(RegisterRequest registerRequest) throws UserAlreadyExistsException;

    LoginResponse login(LoginRequest loginRequest) throws LoginFailedException;

    RefreshTokenResponse refresh(RefreshTokenRequest refreshTokenRequest) throws RefreshTokenFailedException;
}
