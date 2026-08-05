package com.ecommerce.productservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddProductRequest {
    private String name;
    private Double price;
    private String seller;
    private Boolean isAvailable;
}
