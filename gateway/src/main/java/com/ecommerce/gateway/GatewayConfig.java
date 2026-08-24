package com.ecommerce.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class GatewayConfig implements GlobalFilter {

    private final WebClientServices webClientServices;

    public GatewayConfig(WebClientServices webClientServices) {
        this.webClientServices = webClientServices;
    }


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();

        if (path.equals("/auth/login")
                || path.equals("/auth/register")
                || path.equals("/auth/admin/register")
                || path.equals("/auth/admin/login")
                || path.equals("/auth/admin/refresh")
                || path.endsWith("/v3/api-docs")
                || path.equals("/auth/ping")
                || path.equals("/user/ping")) {

            return chain.filter(exchange);
        }

        HttpHeaders header = exchange.getRequest().getHeaders();

        String token = header.getFirst("Authorization");
        if(token == null || !token.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        return webClientServices.authService(exchange, chain, token);
    }
}
