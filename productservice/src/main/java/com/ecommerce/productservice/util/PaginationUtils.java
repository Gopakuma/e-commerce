package com.ecommerce.productservice.util;

import com.ecommerce.productservice.dto.request.PagingResultRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PaginationUtils {

    public static Pageable getPageable(PagingResultRequest request){
        return PageRequest.of(
                request.getPage(),
                request.getSize(),
                request.getDirection(),
                request.getSortBy()
        );
    }
}

