package com.ecommerce_demo.ecommerce.controller;

import com.ecommerce_demo.ecommerce.dto.request.RegisterRequest;
import com.ecommerce_demo.ecommerce.dto.response.RegisterResponse;
import com.ecommerce_demo.ecommerce.service.impl.AuthServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {
    private final AuthServiceImpl authService;

    public AuthController(AuthServiceImpl authService){
        this.authService = authService;
    }

    @PostMapping("/reister")
    public ResponseEntity<RegisterResponse> userRegistration(RegisterRequest registerRequest) {
        try{
        RegisterResponse registerResponse = authService.register(registerRequest);
        return ResponseEntity.ok(registerResponse);
        } catch (RuntimeException e){
            throw new RuntimeException("User Registration failed");
        }
    }
}
