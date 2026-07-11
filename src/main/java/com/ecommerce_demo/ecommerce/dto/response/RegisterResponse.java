package com.ecommerce_demo.ecommerce.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponse {
    private Long id;

    private String firstName;

    private String lastName;

    private String username;

    private String email;

    private String message;
}
