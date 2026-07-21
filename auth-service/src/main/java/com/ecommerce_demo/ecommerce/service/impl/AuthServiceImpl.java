package com.ecommerce_demo.ecommerce.service.impl;

import com.ecommerce_demo.ecommerce.common.enums.Role;
import com.ecommerce_demo.ecommerce.common.enums.TokenType;
import com.ecommerce_demo.ecommerce.dto.request.LoginRequest;
import com.ecommerce_demo.ecommerce.dto.request.RefreshTokenRequest;
import com.ecommerce_demo.ecommerce.dto.request.RegisterRequest;
import com.ecommerce_demo.ecommerce.dto.request.ValidateTokenRequest;
import com.ecommerce_demo.ecommerce.dto.response.LoginResponse;
import com.ecommerce_demo.ecommerce.dto.response.RefreshTokenResponse;
import com.ecommerce_demo.ecommerce.dto.response.RegisterResponse;
import com.ecommerce_demo.ecommerce.dto.response.ValidateTokenResponse;
import com.ecommerce_demo.ecommerce.entity.User;
import com.ecommerce_demo.ecommerce.exception.LoginFailedException;
import com.ecommerce_demo.ecommerce.exception.RefreshTokenFailedException;
import com.ecommerce_demo.ecommerce.exception.UserAlreadyExistsException;
import com.ecommerce_demo.ecommerce.exception.UserRegistrationFailedException;
import com.ecommerce_demo.ecommerce.repository.UserRepository;
import com.ecommerce_demo.ecommerce.security.JwtService;
import com.ecommerce_demo.ecommerce.service.AuthService;
import com.ecommerce_demo.ecommerce.util.Util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl  implements AuthService {

    private final UserRepository userRepository;

    private final Util util;

    private final JwtService jwtService;

    @Value("${jwt.access_expiration}")
    private Long access_expiration;

    @Value("${jwt.refresh_expiration}")
    private Long refresh_expiration;

    public AuthServiceImpl(UserRepository userRepository, Util util, JwtService jwtService) {
        this.userRepository = userRepository;
        this.util = util;
        this.jwtService = jwtService;
    }

    @Override
    public RegisterResponse register(RegisterRequest registerRequest) {
        if(userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new UserAlreadyExistsException("Email already exist");
        }
        if(userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new UserAlreadyExistsException("User Name already exist");
        }

        User newUser = User.builder()
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .password(util.encodePassword(registerRequest.getPassword()))
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .role(Role.CUSTOMER)
                .build();

        try{
            User createdUser =  userRepository.save(newUser);
            return RegisterResponse.builder()
                    .id(createdUser.getId())
                    .firstName(createdUser.getFirstName())
                    .lastName(createdUser.getLastName())
                    .email(createdUser.getEmail())
                    .username(createdUser.getUsername())
                    .message("User Created Successfully !!")
                    .build();
        } catch (Exception e) {
            throw new UserRegistrationFailedException("User creation failed !!");
        }

    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) throws LoginFailedException {
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(()-> new LoginFailedException("Invalid username or password"));

        if(!util.getPasswordEncoder().matches(loginRequest.getPassword(), user.getPassword())) {
           throw new LoginFailedException("Invalid username or password");
        }

        String accessToken = jwtService.generateToken(loginRequest.getUsername(), access_expiration, TokenType.ACCESS);
        String refreshToken = jwtService.generateRefreshToken(loginRequest.getUsername(), refresh_expiration, TokenType.REFRESH);
        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .build();
    }

    @Override
    public RefreshTokenResponse refresh(RefreshTokenRequest refreshTokenRequest) throws RefreshTokenFailedException {
        TokenType tokenType = jwtService.extractTokenType(refreshTokenRequest.getRefreshToken());
        if(tokenType != TokenType.REFRESH){
            throw new RefreshTokenFailedException("Invalid Token");
        }

        String username = jwtService.extractUsername(refreshTokenRequest.getRefreshToken());
        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new LoginFailedException("Invalid username or password"));

        if (!jwtService.validateToken(refreshTokenRequest.getRefreshToken())) {
            throw new RefreshTokenFailedException("Refresh Token is Invalid or Expired");
        }

        String accessToken = jwtService.generateToken(user.getUsername(), access_expiration, TokenType.ACCESS);
        String refreshToken = jwtService.generateRefreshToken(user.getUsername(), refresh_expiration, TokenType.REFRESH);
        return RefreshTokenResponse.builder()
                .refreshToken(refreshToken)
                .accessToken(accessToken)
                .build();
    }

    @Override
    public ValidateTokenResponse validateJwtToken(ValidateTokenRequest validateTokenRequest) {
        String token = validateTokenRequest.getToken();
        if(token == null){
            return ValidateTokenResponse.builder()
                    .isValid(false)
                    .build();
        }
        return ValidateTokenResponse.builder()
                .isValid(jwtService.validateToken(token))
                .build();
    }
}
