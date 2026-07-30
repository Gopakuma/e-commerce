package com.ecommerce_demo.ecommerce.dto.response;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ResetPasswordResponse {
    private String email;
    private String username;
    private String message;
}
