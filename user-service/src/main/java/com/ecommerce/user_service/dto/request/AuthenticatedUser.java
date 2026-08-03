package com.ecommerce.user_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthenticatedUser {
    private String uuid;
    private String email;
    private String username;
    private List<String> role;
}
