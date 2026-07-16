package com.ecommerce_demo.ecommerce.security;

import com.ecommerce_demo.ecommerce.common.enums.TokenType;
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


    public String generateToken(String username, Long expiration, TokenType tokenType){
        return buildToken(username, expiration, tokenType);
    }

    public String generateRefreshToken(String username, Long expiration, TokenType tokenType){
        return buildToken(username, expiration, tokenType);
    }

    public String extractUsername(String token){
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
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

    public String buildToken(String username, Long expiration, TokenType tokenType){
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration( Date.from(
                        Instant.now().plusMillis(expiration)
                ))
                .signWith(getSigningKey())
                .claim("type", tokenType.name())
                .compact();
    }

    public TokenType extractTokenType(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("type", TokenType.class);
    }
}
