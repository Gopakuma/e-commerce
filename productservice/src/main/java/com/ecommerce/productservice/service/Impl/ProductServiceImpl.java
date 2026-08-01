package com.ecommerce.productservice.service.Impl;

import com.ecommerce.productservice.dto.request.PagingResultRequest;
import com.ecommerce.productservice.dto.response.FetchProductResponse;
import com.ecommerce.productservice.dto.response.PagingResultResponse;
import com.ecommerce.productservice.entity.Product;
import com.ecommerce.productservice.exception.FetchProductFailedException;
import com.ecommerce.productservice.repository.ProductRepository;
import com.ecommerce.productservice.service.ProductService;
import com.ecommerce.productservice.util.PaginationUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Getter
@Setter
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public FetchProductResponse fetchProduct(Long id) throws FetchProductFailedException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new FetchProductFailedException("Invalid Product"));

        return FetchProductResponse.builder()
                .name(product.getName())
                .price(product.getPrice())
                .seller(product.getSeller())
                .isAvailable(product.getIsAvailable())
                .build();
    }

    @Override
    public PagingResultResponse<FetchProductResponse> fetchAllProducts(
            int page, int size,
            Sort.Direction direction, String sortBy)
            throws FetchProductFailedException {

        Pageable pageable = PaginationUtils.getPageable(
                PagingResultRequest.builder()
                        .page(page)
                        .size(size)
                        .direction(direction)
                        .sortBy(sortBy)
                        .build());

        Page<Product> products = productRepository.findAll(pageable);

        List<FetchProductResponse> response = products.stream()
                .map(product -> FetchProductResponse.builder()
                        .name(product.getName())
                        .price(product.getPrice())
                        .seller(product.getSeller())
                        .isAvailable(product.getIsAvailable())
                        .build()).toList();


        return PagingResultResponse.<FetchProductResponse>builder()
                .content(response)
                .page(page + 1)
                .direction(direction)
                .sortBy(sortBy)
                .size(size)
                .build();
    }
}
