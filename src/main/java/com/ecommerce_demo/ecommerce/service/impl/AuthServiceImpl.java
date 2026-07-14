package com.ecommerce_demo.ecommerce.service.impl;

import com.ecommerce_demo.ecommerce.common.enums.Role;
import com.ecommerce_demo.ecommerce.dto.request.LoginRequest;
import com.ecommerce_demo.ecommerce.dto.request.RegisterRequest;
import com.ecommerce_demo.ecommerce.dto.response.LoginResponse;
import com.ecommerce_demo.ecommerce.dto.response.RegisterResponse;
import com.ecommerce_demo.ecommerce.entity.User;
import com.ecommerce_demo.ecommerce.exception.LoginFailedException;
import com.ecommerce_demo.ecommerce.exception.UserAlreadyExistsException;
import com.ecommerce_demo.ecommerce.exception.UserRegistrationFailedException;
import com.ecommerce_demo.ecommerce.repository.UserRepository;
import com.ecommerce_demo.ecommerce.security.JwtService;
import com.ecommerce_demo.ecommerce.service.AuthService;
import com.ecommerce_demo.ecommerce.util.Util;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl  implements AuthService {

    private final UserRepository userRepository;

    private final Util util;

    private final JwtService jwtService;

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
            System.out.println("DEBUG REGISTRATION -> Raw: " + registerRequest.getPassword() + " | Encoded: " + newUser.getPassword());
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

        String accessToken = jwtService.generateToken(loginRequest.getUsername());
        return LoginResponse.builder()
                .accessToken(accessToken)
                .tokenType("Bearer")
                .build();
    }
}
