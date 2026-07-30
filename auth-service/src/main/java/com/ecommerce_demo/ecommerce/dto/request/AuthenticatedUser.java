package com.ecommerce_demo.ecommerce.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthenticatedUser {
    private String uuid;
    private String email;
    private String username;
}
