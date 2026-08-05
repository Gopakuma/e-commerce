package com.ecommerce.productservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticatedUser {
    private String userId;
    private String email;
    private List<String> role;
    private String username;
}
