package com.ecommerce.productservice.service;

import com.ecommerce.productservice.dto.request.PagingResultRequest;
import com.ecommerce.productservice.dto.response.FetchProductResponse;
import com.ecommerce.productservice.dto.response.PagingResultResponse;
import com.ecommerce.productservice.exception.FetchProductFailedException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {
    FetchProductResponse fetchProduct(Long id) throws FetchProductFailedException;

    PagingResultResponse<FetchProductResponse> fetchAllProducts(int page, int size, Sort.Direction direction, String sortBy) throws FetchProductFailedException;
}
