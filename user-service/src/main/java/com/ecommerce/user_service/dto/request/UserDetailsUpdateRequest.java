package com.ecommerce.user_service.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserDetailsUpdateRequest {
    private String city;
    private String address;
    private int pincode;
}
