package com.ecommerce_demo.ecommerce.security;

import com.ecommerce_demo.ecommerce.common.enums.TokenType;
import com.ecommerce_demo.ecommerce.dto.request.AuthenticatedUser;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.stream.Collectors;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final CustomUserDetailsService customUserDetailsService;

    public JwtFilter(JwtService jwtService, CustomUserDetailsService customUserDetailsService){
        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {


        String path = request.getRequestURI();

        if(path.startsWith("/auth/register") ||
           path.startsWith("/auth/ping") ||
           path.startsWith("/auth/login") ||
                path.startsWith(("/actuator/health"))
        ) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.substring(7);

        if(jwtService.extractTokenType(jwt) != TokenType.ACCESS){
            filterChain.doFilter(request, response);
            return;
        }

        if(jwtService.validateToken(jwt)) {
            Claims claims = jwtService.extractClaims(jwt);
            String username = claims.getSubject();

            if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){

                UserDetails userDetails =
                        customUserDetailsService.loadUserByUsername(username);

                AuthenticatedUser authenticatedUser = AuthenticatedUser.builder()
                        .uuid(claims.get("uuid", String.class))
                        .username(username)
                        .email(claims.get("email", String.class))
                        .role(userDetails.getAuthorities().stream()
                                .map(grantedAuthority -> grantedAuthority.getAuthority())
                                .collect(Collectors.toList())
                        )
                        .build();

                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                authenticatedUser,
                                null,
                                userDetails.getAuthorities()
                        );
                authenticationToken.setDetails(authenticatedUser);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
