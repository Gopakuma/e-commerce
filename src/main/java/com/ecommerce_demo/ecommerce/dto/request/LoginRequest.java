package com.ecommerce_demo.ecommerce.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    @NotBlank(message = "User name required")
    private String username;
    @NotBlank(message = "Password required")
    private String password;
}
