package com.ecommerce.productservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddProductResponse {
    private String name;
    private Double price;
    private String seller;
    private Boolean isAvailable;
    private String message;
}
