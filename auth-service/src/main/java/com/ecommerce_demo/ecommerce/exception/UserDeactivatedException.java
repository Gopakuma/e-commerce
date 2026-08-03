package com.ecommerce_demo.ecommerce.exception;

public class UserDeactivatedException extends RuntimeException{
    public UserDeactivatedException(String message){
        super(message);
    }
}
