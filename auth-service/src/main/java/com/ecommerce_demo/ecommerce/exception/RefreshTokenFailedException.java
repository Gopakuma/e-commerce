package com.ecommerce_demo.ecommerce.exception;

public class RefreshTokenFailedException extends RuntimeException{
    public RefreshTokenFailedException(String message){
        super(message);
    }
}
