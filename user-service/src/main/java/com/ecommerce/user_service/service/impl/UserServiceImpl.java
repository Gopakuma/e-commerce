package com.ecommerce.user_service.service.impl;

import com.ecommerce.user_service.dto.request.UserDetailsRequest;
import com.ecommerce.user_service.dto.response.UserDetailsResponse;
import com.ecommerce.user_service.entity.User;
import com.ecommerce.user_service.exception.InvalidUserException;
import com.ecommerce.user_service.repository.UserRepository;
import com.ecommerce.user_service.service.UserService;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
@Builder
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDetailsResponse getUserDetails(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new InvalidUserException("Invalid username or email"));

        return UserDetailsResponse.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .build();
    }
}
