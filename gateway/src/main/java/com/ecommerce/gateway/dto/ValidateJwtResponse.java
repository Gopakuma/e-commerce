package com.ecommerce.gateway.dto;

import com.ecommerce.gateway.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ValidateJwtResponse {
    private String email;
    private String username;
    private String uuid;
    private Role role;
}
