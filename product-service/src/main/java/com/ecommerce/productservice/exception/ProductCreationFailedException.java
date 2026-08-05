package com.ecommerce.productservice.exception;

public class ProductCreationFailedException extends RuntimeException {
    public ProductCreationFailedException(String message){
        super(message);
    }
}
