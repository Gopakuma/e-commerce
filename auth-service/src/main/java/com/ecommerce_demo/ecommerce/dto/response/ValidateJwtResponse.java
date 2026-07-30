package com.ecommerce_demo.ecommerce.dto.response;

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
}
