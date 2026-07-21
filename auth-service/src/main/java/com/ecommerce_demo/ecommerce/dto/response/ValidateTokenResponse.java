package com.ecommerce_demo.ecommerce.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ValidateTokenResponse {
    private boolean isValid;
}
