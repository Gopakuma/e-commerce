package com.ecommerce.user_service.controller;

import com.ecommerce.user_service.dto.response.UserDetailsResponse;
import com.ecommerce.user_service.service.impl.UserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class User {

    private UserServiceImpl userService;

    public User(UserServiceImpl userService){
        this.userService = userService;
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserDetailsResponse> fetchUserDetails(@PathVariable("id") Long userId){
        UserDetailsResponse response = userService.getUserDetails(userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
