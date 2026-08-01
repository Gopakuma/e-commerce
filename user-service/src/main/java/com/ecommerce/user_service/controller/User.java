package com.ecommerce.user_service.controller;

import com.ecommerce.user_service.dto.request.UserDetailsUpdateRequest;
import com.ecommerce.user_service.dto.response.UserDetailsResponse;
import com.ecommerce.user_service.dto.response.UserDetailsUpdateResponse;
import com.ecommerce.user_service.service.impl.UserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class User {

    private final UserServiceImpl userService;

    public User(UserServiceImpl userService){
        this.userService = userService;
    }

    @GetMapping("/ping")
    public String ping() {
        return "PONG";
    }

    @GetMapping("/me")
    public ResponseEntity<UserDetailsResponse> fetchUserDetails(
            @RequestHeader("X-User-Id") String userId,
            @RequestHeader("X-User-Email") String email,
            @RequestHeader("X-Username") String username
    ){
        UserDetailsResponse response = userService.getUserDetails(userId, email, username);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/uuid")
    public ResponseEntity<Boolean> createUUID(@RequestBody String uuid) {
        Boolean uuidStatus = userService.createNewUUID(uuid);
        return ResponseEntity.status(HttpStatus.CREATED).body(uuidStatus);
    }

    @PostMapping("/update")
    public ResponseEntity<UserDetailsUpdateResponse> updateUser(
            @RequestHeader("X-User-Id") String userId,
            @RequestBody UserDetailsUpdateRequest userDetailsUpdateRequest) {
        UserDetailsUpdateResponse response = userService.updateUserDetails(userId, userDetailsUpdateRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/securedApi")
    public String securedApi() {
        return "You are Authenticated";
    }
}
