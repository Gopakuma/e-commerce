package com.ecommerce_demo.ecommerce.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LoginRequest {
    @NotBlank(message = "User name required")
    private String username;
    @NotBlank(message = "Password required")
    private String password;
}
