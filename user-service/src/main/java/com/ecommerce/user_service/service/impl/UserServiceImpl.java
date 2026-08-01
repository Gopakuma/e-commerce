package com.ecommerce.user_service.service.impl;

import com.ecommerce.user_service.dto.request.UserDetailsUpdateRequest;
import com.ecommerce.user_service.dto.response.UserDetailsResponse;
import com.ecommerce.user_service.dto.response.UserDetailsUpdateResponse;
import com.ecommerce.user_service.entity.User;
import com.ecommerce.user_service.exception.InvalidUserException;
import com.ecommerce.user_service.exception.UserUpdateFailedException;
import com.ecommerce.user_service.repository.UserRepository;
import com.ecommerce.user_service.service.UserService;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
@Setter
@Builder
@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDetailsResponse getUserDetails(String userId, String email, String username) {
        User user = userRepository.findByUuid(userId)
                .orElseThrow(() -> new InvalidUserException("Invalid username or email"));

        return UserDetailsResponse.builder()
                .email(email)
                .username(username)
                .city(user.getCity())
                .phone(user.getPhone())
                .address(user.getAddress())
                .pincode(user.getPincode())
                .build();
    }

    @Override
    public Boolean createNewUUID(String uuid) {
        User user = User.builder()
                .uuid(uuid)
                .build();
        try {
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            log.error("Failed to save UUID: {}", e.getMessage());
            return false;
        }
    }

    @Override
    @Transactional
    public UserDetailsUpdateResponse updateUserDetails(
            String userId,
            UserDetailsUpdateRequest userDetails) {

        User currentUser = userRepository.findByUuid(userId)
                .orElseThrow(() -> new UserUpdateFailedException("Invalid User"));

        currentUser.setCity(userDetails.getCity());
        currentUser.setPincode(userDetails.getPincode());
        currentUser.setAddress(userDetails.getAddress());
        currentUser.setPhone(userDetails.getPhone());

        userRepository.save(currentUser);

        return UserDetailsUpdateResponse.builder()
                .message("User details updated successfully !!")
                .build();
    }

}
