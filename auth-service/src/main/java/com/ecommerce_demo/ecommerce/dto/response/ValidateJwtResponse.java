package com.ecommerce_demo.ecommerce.dto.response;

import com.ecommerce_demo.ecommerce.common.enums.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ValidateJwtResponse {
    private String email;
    private String username;
    private String uuid;
    private Role role;
}
