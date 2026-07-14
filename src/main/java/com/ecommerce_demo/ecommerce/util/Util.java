package com.ecommerce_demo.ecommerce.util;

import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Getter
public class Util {

    private final PasswordEncoder passwordEncoder;

    public Util(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public String encodePassword(String rawPassword) throws RuntimeException {
        try{
            return passwordEncoder.encode(rawPassword);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }
}
