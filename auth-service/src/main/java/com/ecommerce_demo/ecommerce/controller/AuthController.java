package com.ecommerce_demo.ecommerce.controller;

import com.ecommerce_demo.ecommerce.dto.request.LoginRequest;
import com.ecommerce_demo.ecommerce.dto.request.RefreshTokenRequest;
import com.ecommerce_demo.ecommerce.dto.request.RegisterRequest;
import com.ecommerce_demo.ecommerce.dto.request.ValidateTokenRequest;
import com.ecommerce_demo.ecommerce.dto.response.LoginResponse;
import com.ecommerce_demo.ecommerce.dto.response.RefreshTokenResponse;
import com.ecommerce_demo.ecommerce.dto.response.RegisterResponse;
import com.ecommerce_demo.ecommerce.dto.response.ValidateTokenResponse;
import com.ecommerce_demo.ecommerce.service.impl.AuthServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {
    private final AuthServiceImpl authService;

    public AuthController(AuthServiceImpl authService){
        this.authService = authService;
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

    @GetMapping("/validateToken")
    public Boolean validateToken(@RequestBody ValidateTokenRequest validateTokenRequest){
        ValidateTokenResponse response = authService.validateJwtToken(validateTokenRequest);
        return response.isValid();
    }
}
