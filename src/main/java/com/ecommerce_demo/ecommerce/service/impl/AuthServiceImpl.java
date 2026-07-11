package com.ecommerce_demo.ecommerce.service.impl;

import com.ecommerce_demo.ecommerce.dto.request.RegisterRequest;
import com.ecommerce_demo.ecommerce.dto.response.RegisterResponse;
import com.ecommerce_demo.ecommerce.entity.User;
import com.ecommerce_demo.ecommerce.exception.UserAlreadyExistsException;
import com.ecommerce_demo.ecommerce.repository.UserRepository;
import com.ecommerce_demo.ecommerce.service.AuthService;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl  implements AuthService {

    private final UserRepository userRepository;

    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public RegisterResponse register(RegisterRequest registerRequest) throws RuntimeException {
        User newUser = User.builder()
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .password(registerRequest.getPassword())
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .build();

        boolean existingEmail = userRepository.existsByEmail(registerRequest.getEmail());
        boolean existingUserName = userRepository.existsByUsername(registerRequest.getUsername());

        if(!existingEmail && !existingUserName) {
        User createdUser =  userRepository.save(newUser);
        return RegisterResponse.builder()
                .id(createdUser.getId())
                .firstName(createdUser.getFirstName())
                .lastName(createdUser.getLastName())
                .email(createdUser.getEmail())
                .username(createdUser.getUsername())
                .build();
        } else {
            throw new UserAlreadyExistsException("User Already exist");
        }
    }
}
