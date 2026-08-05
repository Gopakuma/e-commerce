package com.ecommerce.productservice.security;

import com.ecommerce.productservice.dto.request.AuthenticatedUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class GatewayFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        String userId = request.getHeader("X-User-Id");
        String email = request.getHeader("X-User-Email");
        String username = request.getHeader("X-Username");
        String role = request.getHeader("X-User-Role");

        String path = request.getRequestURI();

        if(path.startsWith("/healthcheck") ||
                path.startsWith("/v3/api-docs/**") ||
                path.startsWith("/product/v3/api-docs/**") ||
                path.startsWith("/actuator/health") ||
                path.startsWith("/swagger-ui/**")
        ) {
            filterChain.doFilter(request, response);
            return;
        }


        if(userId != null && role != null) {
            AuthenticatedUser authenticatedUser = AuthenticatedUser.builder()
                    .userId(userId)
                    .email(email)
                    .username(username)
                    .role(List.of("ROLE_" + role))
                    .build();

            List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(
                    "ROLE_" + role
            ));

            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    authenticatedUser,
                    null,
                    authorities
            );

            SecurityContextHolder.getContext().setAuthentication(token);
        }

        filterChain.doFilter(request, response);

    }
}
