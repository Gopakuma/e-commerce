package com.ecommerce_demo.ecommerce.exception;

public class UserRegistrationFailedException extends RuntimeException{
    public UserRegistrationFailedException(String message){
        super(message);
    }
}
