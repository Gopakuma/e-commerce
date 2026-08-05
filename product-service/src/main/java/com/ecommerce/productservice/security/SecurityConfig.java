package com.ecommerce.productservice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final GatewayFilter gatewayFilter;

    public SecurityConfig(GatewayFilter gatewayFilter) {
        this.gatewayFilter = gatewayFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        return http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(httpSession->
                        httpSession.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        )
                .authorizeHttpRequests(auth->
                        auth.requestMatchers(
                                        "/healthcheck",
                                        "/v3/api-docs/**",
                                        "/product/v3/api-docs/**",
                                        "/actuator/health",
                                        "/swagger-ui/**",
                                        "/swagger-ui.html"

                                ).permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(gatewayFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}

