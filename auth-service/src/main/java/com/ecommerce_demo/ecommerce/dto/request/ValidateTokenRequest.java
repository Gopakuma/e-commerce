package com.ecommerce_demo.ecommerce.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidateTokenRequest {

    private String token;
}
