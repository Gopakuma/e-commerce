package com.ecommerce_demo.ecommerce.exception;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordFailedException extends RuntimeException {
    public ResetPasswordFailedException(String message){
        super(message);
    }
}
