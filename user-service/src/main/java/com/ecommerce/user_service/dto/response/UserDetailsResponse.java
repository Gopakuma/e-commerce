package com.ecommerce.user_service.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserDetailsResponse {

    private String username;
    private String email;

}
