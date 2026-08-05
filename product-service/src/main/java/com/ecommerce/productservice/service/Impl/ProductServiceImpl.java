package com.ecommerce.productservice.service.Impl;

import com.ecommerce.productservice.commons.ErrorMessageConstants;
import com.ecommerce.productservice.commons.SuccessMessageConstants;
import com.ecommerce.productservice.dto.request.AddProductRequest;
import com.ecommerce.productservice.dto.request.PagingResultRequest;
import com.ecommerce.productservice.dto.response.AddProductResponse;
import com.ecommerce.productservice.dto.response.DeleteProductResponse;
import com.ecommerce.productservice.dto.response.FetchProductResponse;
import com.ecommerce.productservice.dto.response.PagingResultResponse;
import com.ecommerce.productservice.entity.Product;
import com.ecommerce.productservice.exception.FetchProductFailedException;
import com.ecommerce.productservice.exception.ProductCreationFailedException;
import com.ecommerce.productservice.repository.ProductRepository;
import com.ecommerce.productservice.service.ProductService;
import com.ecommerce.productservice.util.PaginationUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
                .orElseThrow(() -> new FetchProductFailedException(ErrorMessageConstants.PRODUCT_NOT_FOUND));

        return FetchProductResponse.builder()
                .name(product.getName())
                .price(product.getPrice())
                .seller(product.getSeller())
                .isAvailable(product.getIsAvailable())
                .message(SuccessMessageConstants.FETCH_PRODUCT_SUCCESS)
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
                        .message(SuccessMessageConstants.FETCH_PRODUCT_SUCCESS)
                        .build()).toList();


        return PagingResultResponse.<FetchProductResponse>builder()
                .content(response)
                .page(page + 1)
                .direction(direction)
                .sortBy(sortBy)
                .size(size)
                .build();
    }

    @Override
    @Transactional
    public AddProductResponse createProduct(AddProductRequest request) throws ProductCreationFailedException {
        if(productRepository.existsByName(request.getName())) {
            throw new ProductCreationFailedException(ErrorMessageConstants.PRODUCT_EXITS);
        }

            productRepository.save(Product.builder()
                        .name(request.getName())
                        .price(request.getPrice())
                        .seller(request.getSeller())
                        .isAvailable(request.getIsAvailable())
                        .build());

           Product newProduct = productRepository.findByName(request.getName())
                    .orElseThrow(()-> new ProductCreationFailedException(ErrorMessageConstants.PRODUCT_CREATION_FAILED));

        return AddProductResponse.builder()
                .name(newProduct.getName())
                .price(newProduct.getPrice())
                .seller(newProduct.getSeller())
                .isAvailable(newProduct.getIsAvailable())
                .message(SuccessMessageConstants.PRODUCT_CREATION_SUCCESS)
                .build();
    }

    @Override
    @Transactional
    public DeleteProductResponse deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(()-> new FetchProductFailedException(ErrorMessageConstants.PRODUCT_NOT_FOUND));

        productRepository.deleteById(id);

        return DeleteProductResponse.builder()
                .id(product.getId())
                .message(SuccessMessageConstants.PRODUCT_DELETION_SUCCESS)
                .dateTime(LocalDateTime.now())
                .build();
    }
}
