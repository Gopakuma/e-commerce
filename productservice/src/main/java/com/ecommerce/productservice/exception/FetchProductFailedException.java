package com.ecommerce.productservice.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class FetchProductFailedException extends RuntimeException {
    public FetchProductFailedException(String message){
        super(message);
    }
}
