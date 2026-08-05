package com.ecommerce.user_service.service.impl;

import com.ecommerce.user_service.commons.ErrorMessageConstants;
import com.ecommerce.user_service.commons.SuccessMessageConstants;
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

import java.util.UUID;

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
                .orElseThrow(() -> new InvalidUserException(ErrorMessageConstants.INVALID_CREDENTIALS));

        return UserDetailsResponse.builder()
                .email(email)
                .username(username)
                .city(user.getCity())
                .phone(user.getPhone())
                .address(user.getAddress())
                .pincode(user.getPincode())
                .message(SuccessMessageConstants.FETCH_USER_SUCCESS)
                .build();
    }

    @Override
    public Boolean createNewUUID(UUID uuid) {
        User user = User.builder()
                .uuid(uuid.toString())
                .build();
        try {
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            System.out.println(ErrorMessageConstants.UUID_CREATION_FAILED + e.getMessage());
            return false;
        }
    }

    @Override
    @Transactional
    public UserDetailsUpdateResponse updateUserDetails(UserDetailsUpdateRequest userDetails) {
        User currentUser = userRepository.findByUuid(userDetails.getUserId())
                .orElseThrow(() -> new UserUpdateFailedException(ErrorMessageConstants.USER_NOT_FOUND));

        currentUser.setCity(userDetails.getCity());
        currentUser.setPincode(userDetails.getPincode());
        currentUser.setAddress(userDetails.getAddress());
        currentUser.setPhone(userDetails.getPhone());

        userRepository.save(currentUser);

        User updatedUser = userRepository.findByUuid(userDetails.getUserId())
                .orElseThrow(() -> new UserUpdateFailedException(ErrorMessageConstants.USER_UPDATE_FAILED));

        return UserDetailsUpdateResponse.builder()
                .email(userDetails.getEmail())
                .address(updatedUser.getAddress())
                .city(updatedUser.getCity())
                .phone(updatedUser.getPhone())
                .pincode(updatedUser.getPincode())
                .message(SuccessMessageConstants.USER_UPDATE_SUCCESS)
                .build();
    }

}
