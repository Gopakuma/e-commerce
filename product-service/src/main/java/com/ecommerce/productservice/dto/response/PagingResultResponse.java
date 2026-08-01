package com.ecommerce.productservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

import java.util.Collection;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagingResultResponse<T> {

    private Collection<T> content;
    private Integer page;
    private Integer size;
    private String sortBy;
    private Sort.Direction direction;
}
