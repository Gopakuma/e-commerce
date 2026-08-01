package com.ecommerce.productservice.dto.request;

import lombok.*;
import org.springframework.data.domain.Sort;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PagingResultRequest<T> {

    @Builder.Default
    private Integer page = 0;

    @Builder.Default
    private Integer size = 10;

    @Builder.Default
    private String sortBy = "Id";

    @Builder.Default
    private Sort.Direction direction = Sort.Direction.ASC;

}
