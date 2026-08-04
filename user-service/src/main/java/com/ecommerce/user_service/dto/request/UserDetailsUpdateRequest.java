package com.ecommerce.user_service.dto.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsUpdateRequest {
    private String userId;
    private String email;
    private String city;
    private String address;
    private int pincode;
    private String phone;
}
