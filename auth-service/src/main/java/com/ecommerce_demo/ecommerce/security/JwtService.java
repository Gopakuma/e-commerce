package com.ecommerce_demo.ecommerce.security;

import com.ecommerce_demo.ecommerce.common.enums.Role;
import com.ecommerce_demo.ecommerce.common.enums.TokenType;
import com.ecommerce_demo.ecommerce.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }


    public String generateToken(User user, Long expiration, TokenType tokenType, Role role){
        return buildToken(user, expiration, tokenType, role);
    }

    public Claims extractClaims(String token){
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token){
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);

            return true;
        } catch (JwtException e){
            return false;
        }
    }

    public String buildToken(User user, Long expiration, TokenType tokenType, Role role){
        return Jwts.builder()
                .subject(user.getUsername())
                .issuedAt(new Date())
                .expiration( Date.from(
                        Instant.now().plusMillis(expiration)
                ))
                .signWith(getSigningKey())
                .claim("uuid", user.getUuid())
                .claim("email", user.getEmail())
                .claim("type", tokenType.name())
                .claim("role", role)
                .compact();
    }

    public TokenType extractTokenType(String token) {
        String type =  Jwts.parser()
                        .verifyWith(getSigningKey())
                        .build()
                        .parseSignedClaims(token)
                        .getPayload()
                        .get("type", String.class);
        return TokenType.valueOf(type);
    }
}
