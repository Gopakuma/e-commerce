package com.ecommerce_demo.ecommerce.service.impl;

import com.ecommerce_demo.ecommerce.common.enums.Role;
import com.ecommerce_demo.ecommerce.dto.request.RegisterRequest;
import com.ecommerce_demo.ecommerce.dto.response.RegisterResponse;
import com.ecommerce_demo.ecommerce.entity.User;
import com.ecommerce_demo.ecommerce.exception.UserAlreadyExistsException;
import com.ecommerce_demo.ecommerce.exception.UserRegistrationFailedException;
import com.ecommerce_demo.ecommerce.repository.UserRepository;
import com.ecommerce_demo.ecommerce.service.AuthService;
import com.ecommerce_demo.ecommerce.util.Util;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl  implements AuthService {

    private final UserRepository userRepository;

    private final Util util;

    public AuthServiceImpl(UserRepository userRepository, Util util) {
        this.userRepository = userRepository;
        this.util = util;
    }

    @Override
    public RegisterResponse register(RegisterRequest registerRequest) {
        User newUser = User.builder()
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .password(util.encodePassword(registerRequest.getPassword()))
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .role(Role.CUSTOMER)
                .build();

        if(userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new UserAlreadyExistsException("Email already exist");
        }
        if(userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new UserAlreadyExistsException("User Name already exist");
        }
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
}
