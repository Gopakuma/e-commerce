package com.ecommerce_demo.ecommerce.controller;

import com.ecommerce_demo.ecommerce.dto.request.*;
import com.ecommerce_demo.ecommerce.dto.response.*;
import com.ecommerce_demo.ecommerce.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @GetMapping("/ping")
    public String ping() {
        return "PONG";
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> userRegistration(@Valid @RequestBody RegisterRequest registerRequest) {
            RegisterResponse response = authService.register(registerRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse response = authService.login(loginRequest );
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponse> refresh(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        RefreshTokenResponse response = authService.refresh(refreshTokenRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/validateUser")
    public ValidateJwtResponse validateUser(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String header)
    {
        return authService.validateJwtToken(header);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ResetPasswordResponse> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        ResetPasswordResponse response = authService.resetPasswordService(resetPasswordRequest);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }
}
