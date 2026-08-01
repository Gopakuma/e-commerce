package com.ecommerce_demo.ecommerce.exception;

public class ResetPasswordFailedException extends RuntimeException {
    public ResetPasswordFailedException(String message){
        super(message);
    }
}
