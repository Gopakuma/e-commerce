package com.ecommerce.productservice.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FetchProductResponse {
    private String name;
    private String seller;
    private Double price;
    private Boolean isAvailable;
}
