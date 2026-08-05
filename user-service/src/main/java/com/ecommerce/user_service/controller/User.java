package com.ecommerce.user_service.controller;

import com.ecommerce.user_service.commons.SuccessMessageConstants;
import com.ecommerce.user_service.dto.request.UserDetailsUpdateRequest;
import com.ecommerce.user_service.dto.response.UserDetailsResponse;
import com.ecommerce.user_service.dto.response.UserDetailsUpdateResponse;
import com.ecommerce.user_service.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/user")
public class User {

    private final UserService userService;

    public User(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/ping")
    public String ping() {
        return "PONG";
    }

    @GetMapping("/me")
    public ResponseEntity<UserDetailsResponse> fetchUserDetails(
            @Parameter(hidden = true)
            @RequestHeader("X-User-Id") String userId,
            @Parameter(hidden = true)
            @RequestHeader("X-User-Email") String email,
            @Parameter(hidden = true)
            @RequestHeader("X-Username") String username
    ){
        UserDetailsResponse response = userService.getUserDetails(userId, email, username);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/uuid")
    public ResponseEntity<Boolean> createUUID(@RequestBody UUID uuid) {
        Boolean uuidStatus = userService.createNewUUID(uuid);
        return ResponseEntity.status(HttpStatus.CREATED).body(uuidStatus);
    }

    @PostMapping("/update")
    public ResponseEntity<UserDetailsUpdateResponse> updateUser(
            @Parameter(hidden = true)
            @RequestHeader("X-User-Id") String userId,
            @Parameter(hidden = true)
            @RequestHeader("X-User-Email") String email,
            @RequestBody UserDetailsUpdateRequest userDetailsUpdateRequest) {

        userDetailsUpdateRequest.setEmail(email);
        userDetailsUpdateRequest.setUserId(userId);

        UserDetailsUpdateResponse response = userService.updateUserDetails(userDetailsUpdateRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @GetMapping("/securedApi")
    public String securedApi() {
        return SuccessMessageConstants.AUTHENTICATION_SUCCESS;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/adminOnly")
    public String adminOnly() {
        return SuccessMessageConstants.AUTHORIZATION_SUCCESS;}
}
