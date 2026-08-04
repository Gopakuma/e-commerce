package com.ecommerce.user_service.service;

import com.ecommerce.user_service.dto.request.UserDetailsUpdateRequest;
import com.ecommerce.user_service.dto.response.UserDetailsResponse;
import com.ecommerce.user_service.dto.response.UserDetailsUpdateResponse;
import com.ecommerce.user_service.exception.InvalidUserException;
import com.ecommerce.user_service.exception.UserUpdateFailedException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface UserService {
    UserDetailsResponse getUserDetails(String userId, String email, String username) throws InvalidUserException;

    UserDetailsUpdateResponse updateUserDetails(UserDetailsUpdateRequest userDetailsUpdateRequest) throws UserUpdateFailedException;

    Boolean createNewUUID(UUID uuid);
}
