package com.ecommerce_demo.ecommerce.controller;

import com.ecommerce_demo.ecommerce.common.enums.Role;
import com.ecommerce_demo.ecommerce.dto.request.LoginRequest;
import com.ecommerce_demo.ecommerce.dto.request.RefreshTokenRequest;
import com.ecommerce_demo.ecommerce.dto.request.RegisterRequest;
import com.ecommerce_demo.ecommerce.dto.response.LoginResponse;
import com.ecommerce_demo.ecommerce.dto.response.RefreshTokenResponse;
import com.ecommerce_demo.ecommerce.dto.response.RegisterResponse;
import com.ecommerce_demo.ecommerce.entity.User;
import com.ecommerce_demo.ecommerce.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth/admin")
public class AdminController {

    private final AuthService authService;

    public AdminController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> adminRegistration(@Valid @RequestBody RegisterRequest registerRequest) {
        RegisterResponse response = authService.register(registerRequest, Role.ADMIN);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse response = authService.login(loginRequest, Role.ADMIN);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponse> refresh(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        RefreshTokenResponse response = authService.refresh(refreshTokenRequest, Role.ADMIN);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<List<User>> fetchAllUsers(@RequestParam(value = "enabled", required = false) Boolean enabled) {
        List<User> response = authService.fetchAllUsers(enabled);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/updateUser")
    public ResponseEntity<User> updateUserStatus(@RequestParam("email") String email, @RequestParam("enabled") Boolean enabled) {
        User response = authService.updateUserStatus(email, enabled);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
