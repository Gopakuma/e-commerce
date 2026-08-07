package com.ecommerce_demo.ecommerce.service.impl;

import com.ecommerce_demo.ecommerce.common.constants.ErrorMessageConstants;
import com.ecommerce_demo.ecommerce.common.constants.SuccessMessageConstants;
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
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class AuthServiceImpl  implements AuthService {

    private final UserRepository userRepository;

    private final JwtService jwtService;

    private final UserServiceClient userServiceClient;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.access_expiration}")
    private Long access_expiration;

    @Value("${jwt.refresh_expiration}")
    private Long refresh_expiration;

    public AuthServiceImpl(
            UserRepository userRepository,
            JwtService jwtService,
            UserServiceClient userServiceClient, PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.userServiceClient = userServiceClient;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public RegisterResponse register(RegisterRequest registerRequest,Role role) {
        if(userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new UserAlreadyExistsException(ErrorMessageConstants.EMAIL_EXISTS);
        }
        if(userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new UserAlreadyExistsException(ErrorMessageConstants.USERNAME_EXISTS);
        }

        UUID uuid = UUID.randomUUID();

        Boolean uuidStatus = userServiceClient.updateUuid(uuid);
        
        if (!uuidStatus) {
            throw new UserRegistrationFailedException(ErrorMessageConstants.UUID_EXISTS);
        }

        User newUser = User.builder()
                .uuid(uuid.toString())
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .role(role)
                .build();

        try{
            User createdUser =  userRepository.save(newUser);
            return RegisterResponse.builder()
                    .id(createdUser.getId())
                    .firstName(createdUser.getFirstName())
                    .lastName(createdUser.getLastName())
                    .email(createdUser.getEmail())
                    .username(createdUser.getUsername())
                    .message(SuccessMessageConstants.USER_REGISTERED)
                    .build();
        } catch (Exception e) {
            throw new UserRegistrationFailedException(ErrorMessageConstants.USER_REGISTRATION_FAILED);
        }

    }

    @Override
    public LoginResponse login(LoginRequest loginRequest, Role role) throws LoginFailedException {
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(()-> new LoginFailedException(ErrorMessageConstants.INVALID_CREDENTIALS));

        if(user.getRole() != role) {
            throw new LoginFailedException(ErrorMessageConstants.INVALID_CREDENTIALS);
        }

        if(!user.isEnabled()) {
            throw new UserDeactivatedException(ErrorMessageConstants.ACCOUNT_DISABLED);
        }

        if(!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
           throw new LoginFailedException(ErrorMessageConstants.INVALID_CREDENTIALS);
        }

        String accessToken = jwtService.generateToken(
                user,
                access_expiration,
                TokenType.ACCESS,
                role
                );

        String refreshToken = jwtService.generateToken(
                user,
                refresh_expiration,
                TokenType.REFRESH,
                role
                );

        user.setRefreshToken(refreshToken);
        userRepository.save(user);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .tokenType("Bearer")
                .message(SuccessMessageConstants.LOGIN_SUCCESSFUL)
                .build();
    }

    @Override
    public RefreshTokenResponse refresh(RefreshTokenRequest refreshTokenRequest, Role role) throws RefreshTokenFailedException {
        TokenType tokenType = jwtService.extractTokenType(refreshTokenRequest.getAccessToken());

        if(tokenType != TokenType.ACCESS){
            throw new RefreshTokenFailedException(ErrorMessageConstants.TOKEN_INVALID);
        }

        Claims claims = jwtService.extractClaims(refreshTokenRequest.getAccessToken());

        String username = claims.getSubject();
        String requestRole = claims.get("Role", String.class);

        if(!requestRole.equals(role.name())) {
            throw new RefreshTokenFailedException(ErrorMessageConstants.ACCESS_DENIED);
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new LoginFailedException(ErrorMessageConstants.INVALID_CREDENTIALS));

        if(!user.isEnabled()) {
            throw new UserDeactivatedException(ErrorMessageConstants.ACCOUNT_DISABLED);
        }

        if (!jwtService.validateToken(user.getRefreshToken())) {
            throw new RefreshTokenFailedException(ErrorMessageConstants.REFRESH_TOKEN_EXPIRED);
        }

        String accessToken = jwtService.generateToken(
                user,
                access_expiration,
                TokenType.ACCESS,
                role
                );

        String refreshToken = jwtService.generateToken(
                user,
                refresh_expiration,
                TokenType.REFRESH,
                role
                );
        user.setRefreshToken(refreshToken);
        userRepository.save(user);

        return RefreshTokenResponse.builder()
                .accessToken(accessToken)
                .message(SuccessMessageConstants.TOKEN_GENERATION_SUCCESS)
                .build();
    }

    @Override
    public ValidateJwtResponse validateJwtToken(String header) {
        String token = header.substring(7);
        Claims claims = jwtService.extractClaims(token);
        String email = claims.get("email", String.class);
        String role = claims.get("role", String.class);

        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException(ErrorMessageConstants.USER_NOT_FOUND));

        if(!user.isEnabled()) {
            throw new UserDeactivatedException(ErrorMessageConstants.ACCOUNT_DISABLED);
        }

        return ValidateJwtResponse.builder()
                .email(claims.get("email", String.class))
                .username(claims.getSubject())
                .uuid(claims.get("uuid", String.class))
                .role(Role.valueOf(role))
                .build();
    }

    @Override
    @Transactional
    public ResetPasswordResponse resetPasswordService(ResetPasswordRequest request) throws ResetPasswordFailedException {
        User existingUser = userRepository.findByEmail(request.getEmail())
                .orElseThrow(()-> new ResetPasswordFailedException(ErrorMessageConstants.INVALID_CREDENTIALS));

        if(!passwordEncoder.matches(
                request.getOldPassword(),
                existingUser.getPassword())
        ){
            throw new ResetPasswordFailedException(ErrorMessageConstants.INVALID_CREDENTIALS);
        }

        if(passwordEncoder.matches(
                request.getNewPassword(),
                existingUser.getPassword())
        ){
            throw new ResetPasswordFailedException(ErrorMessageConstants.INVALID_NEW_PASSWORD);
        }

        existingUser.setPassword(passwordEncoder.encode(request.getNewPassword()));

        try {
            userRepository.save(existingUser);
            return ResetPasswordResponse.builder()
                    .email(existingUser.getEmail())
                    .username(existingUser.getUsername())
                    .message(SuccessMessageConstants.PASSWORD_RESET_SUCCESS)
                    .build();
        } catch (Exception ex) {
            throw new ResetPasswordFailedException(ErrorMessageConstants.PASSWORD_RESET_FAILED + ex.getMessage());
        }

    }

    @Override
    public List<User> fetchAllUsers(Boolean enabled) {
        if(enabled == null) {
            return userRepository.findAll();
        }
        return userRepository.findByEnabled(enabled);
    }

    @Override
    public User updateUserStatus(String email, Boolean enabled ) {

        int updateRows = userRepository.updateUserStatus(email, enabled);

        if(updateRows == 0) {
            throw new RuntimeException(ErrorMessageConstants.USER_NOT_FOUND);
        }

        return userRepository.findByEmail(email)
                .orElseThrow(()-> new RuntimeException(ErrorMessageConstants.USER_NOT_FOUND));
    }
}
