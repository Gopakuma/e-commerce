package com.ecommerce_demo.ecommerce.service.impl;

import com.ecommerce_demo.ecommerce.common.enums.Role;
import com.ecommerce_demo.ecommerce.common.enums.TokenType;
import com.ecommerce_demo.ecommerce.dto.request.*;
import com.ecommerce_demo.ecommerce.dto.response.*;
import com.ecommerce_demo.ecommerce.entity.User;
import com.ecommerce_demo.ecommerce.exception.*;
import com.ecommerce_demo.ecommerce.repository.UserRepository;
import com.ecommerce_demo.ecommerce.security.JwtService;
import com.ecommerce_demo.ecommerce.service.AuthService;
import com.ecommerce_demo.ecommerce.util.UserServiceClient;
import com.ecommerce_demo.ecommerce.util.Util;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class AuthServiceImpl  implements AuthService {

    private final UserRepository userRepository;

    private final Util util;

    private final JwtService jwtService;

    private final UserServiceClient userServiceClient;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.access_expiration}")
    private Long access_expiration;

    @Value("${jwt.refresh_expiration}")
    private Long refresh_expiration;

    public AuthServiceImpl(
            UserRepository userRepository,
            Util util,
            JwtService jwtService,
            UserServiceClient userServiceClient, PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.util = util;
        this.jwtService = jwtService;
        this.userServiceClient = userServiceClient;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public RegisterResponse register(RegisterRequest registerRequest) {
        if(userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new UserAlreadyExistsException("Email already exist");
        }
        if(userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new UserAlreadyExistsException("User Name already exist");
        }

        UUID uuid = UUID.randomUUID();

        Boolean uuidStatus = userServiceClient.updateUuid(uuid);
        
        if (!uuidStatus) {
            throw new UserRegistrationFailedException("Registration failed: The requested UUID is invalid or already taken");
        }

        User newUser = User.builder()
                .uuid(uuid.toString())
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

        String accessToken = jwtService.generateToken(
                user,
                access_expiration,
                TokenType.ACCESS);

        String refreshToken = jwtService.generateRefreshToken(
                user,
                refresh_expiration,
                TokenType.REFRESH);

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

        String accessToken = jwtService.generateToken(
                user,
                access_expiration,
                TokenType.ACCESS);

        String refreshToken = jwtService.generateRefreshToken(
                user,
                refresh_expiration,
                TokenType.REFRESH);
        return RefreshTokenResponse.builder()
                .refreshToken(refreshToken)
                .accessToken(accessToken)
                .build();
    }

    @Override
    public ValidateJwtResponse validateJwtToken(String header) {
        String token = header.substring(7);
        return ValidateJwtResponse.builder()
                .email(jwtService.extractEmail(token))
                .username(jwtService.extractUsername(token))
                .uuid(jwtService.extractUuid(token))
                .build();
    }

    @Override
    @Transactional
    public ResetPasswordResponse resetPasswordService(ResetPasswordRequest request) throws ResetPasswordFailedException {
        User existingUser = userRepository.findByEmail(request.getEmail())
                .orElseThrow(()-> new ResetPasswordFailedException("Invalid email or password"));

        if(!passwordEncoder.matches(
                request.getOldPassword(),
                existingUser.getPassword())
        ){
            throw new ResetPasswordFailedException("Invalid email or password");
        }

        if(passwordEncoder.matches(
                request.getNewPassword(),
                existingUser.getPassword())
        ){
            throw new ResetPasswordFailedException("New password should different from Old password");
        }

        existingUser.setPassword(passwordEncoder.encode(request.getNewPassword()));

        try {
            userRepository.save(existingUser);
            return ResetPasswordResponse.builder()
                    .email(existingUser.getEmail())
                    .username(existingUser.getUsername())
                    .message("Reset Password Successfully !")
                    .build();
        } catch (Exception ex) {
            throw new ResetPasswordFailedException("Password Reset Failed: " + ex.getMessage());
        }

    }
}
