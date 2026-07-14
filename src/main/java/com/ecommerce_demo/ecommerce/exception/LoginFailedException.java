package com.ecommerce_demo.ecommerce.exception;

public class LoginFailedException extends RuntimeException{
    public LoginFailedException(String message){
        super(message);
    }
}
