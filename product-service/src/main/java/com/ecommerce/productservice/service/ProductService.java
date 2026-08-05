package com.ecommerce.productservice.service;

import com.ecommerce.productservice.dto.request.AddProductRequest;
import com.ecommerce.productservice.dto.response.AddProductResponse;
import com.ecommerce.productservice.dto.response.DeleteProductResponse;
import com.ecommerce.productservice.dto.response.FetchProductResponse;
import com.ecommerce.productservice.dto.response.PagingResultResponse;
import com.ecommerce.productservice.exception.FetchProductFailedException;
import com.ecommerce.productservice.exception.ProductCreationFailedException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public interface ProductService {
    FetchProductResponse fetchProduct(Long id) throws FetchProductFailedException;

    PagingResultResponse<FetchProductResponse> fetchAllProducts(int page, int size, Sort.Direction direction, String sortBy) throws FetchProductFailedException;

    AddProductResponse createProduct(AddProductRequest request) throws ProductCreationFailedException;

    DeleteProductResponse deleteProduct(Long id);
}
