package com.ecommerce.user_service.service;

import com.ecommerce.user_service.dto.response.UserDetailsResponse;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    UserDetailsResponse getUserDetails(Long userId);
}
