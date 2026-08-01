package com.ecommerce.productservice.exception;

public class FetchProductFailedException extends RuntimeException {
    public FetchProductFailedException(String message){
        super(message);
    }
}
