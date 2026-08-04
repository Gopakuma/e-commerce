package com.ecommerce.user_service.security;

import com.ecommerce.user_service.dto.request.AuthenticatedUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;

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
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        System.out.println("===== USER SERVICE FILTER HIT =====");

        String path = request.getRequestURI();

        if (path.contains("/v3/api-docs") ||
                path.contains("/swagger-ui") ||
                path.contains("/swagger-ui.html") ||
                path.equals("/user/uuid") ||
                path.contains("/actuator/health")) {

            filterChain.doFilter(request, response);
            return;
        }


        String userId  = request.getHeader("X-User-Id");
        String username = request.getHeader("X-Username");
        String email = request.getHeader("X-User-Email");
        String role = request.getHeader("X-User-Role");

        if(role != null && username != null) {
            System.out.println("===== USER SERVICE FILTER CONDITION HIT =====");
            List<SimpleGrantedAuthority> grantedAuthorities = List.of(
                    new SimpleGrantedAuthority("ROLE_" + role));

            AuthenticatedUser principalUser = AuthenticatedUser.builder()
                    .uuid(userId)
                    .username(username)
                    .email(email)
                    .role(List.of("ROLE_" + role))
                    .build();

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            principalUser,
                            null,
                            grantedAuthorities
                    );

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            filterChain.doFilter(request, response);
        }
    }
}
