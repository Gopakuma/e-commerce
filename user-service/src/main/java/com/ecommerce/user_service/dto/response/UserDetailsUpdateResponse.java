package com.ecommerce.user_service.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsUpdateResponse {
    private String email;
    private String phone;
    private String city;
    private int pincode;
    private String address;
    private String message;
}
