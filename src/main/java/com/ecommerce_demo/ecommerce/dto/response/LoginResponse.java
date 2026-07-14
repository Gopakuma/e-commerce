package com.ecommerce_demo.ecommerce.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
public class LoginResponse {
    private String accessToken;
    private String tokenType;
}