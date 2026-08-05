package com.ecommerce.productservice.controller;

import com.ecommerce.productservice.dto.request.AddProductRequest;
import com.ecommerce.productservice.dto.response.AddProductResponse;
import com.ecommerce.productservice.dto.response.FetchProductResponse;
import com.ecommerce.productservice.dto.response.PagingResultResponse;
import com.ecommerce.productservice.service.ProductService;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/fetchProduct/{id}")
    public ResponseEntity<FetchProductResponse> fetchProduct(@PathVariable("id") Long id) {
        FetchProductResponse response = productService.fetchProduct(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/fetchAllProducts")
    public ResponseEntity<PagingResultResponse<FetchProductResponse>> fetchAllProducts(
            @RequestParam(required = false) int page,
            @RequestParam(required = false) int size,
            @RequestParam(required = false) Sort.Direction direction,
            @RequestParam(required = false) String sortBy
            ){
        PagingResultResponse<FetchProductResponse> response = productService.fetchAllProducts(page , size, direction, sortBy);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/product")
    public ResponseEntity<AddProductResponse> addProduct(@RequestBody AddProductRequest request) {
        AddProductResponse response = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
