package com.ecommerce.gateway;

import com.ecommerce.gateway.dto.ValidateJwtResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class WebClientServices {

    private final WebClient serviceClient;

    public WebClientServices(WebClient serviceClient) {
        this.serviceClient = serviceClient;
    }

    @CircuitBreaker(
            name = "authService",
            fallbackMethod = "fallback"
    )
    public Mono<Void> authService(ServerWebExchange exchange, GatewayFilterChain chain, String token) {
       return serviceClient
                .get()
                .uri("lb://auth-service/auth/validateUser")
                .header(HttpHeaders.AUTHORIZATION, token)
                .retrieve()
                .bodyToMono(ValidateJwtResponse.class)
                .flatMap(response ->{
                    ServerHttpRequest request = exchange.getRequest().mutate()
                            .header("X-User-Id", response.getUuid())
                            .header("X-User-Email", response.getEmail())
                            .header("X-Username", response.getUsername())
                            .header("X-User-Role", response.getRole().name())
                            .build();
                    return chain.filter(exchange.mutate().request(request).build());
                })
                .onErrorResume(ex -> {
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    log.error(ex.getMessage());
                    return exchange.getResponse().setComplete();
                });
         }

    @SuppressWarnings("unused")
    public Mono<Void> fallback(ServerWebExchange exchange, GatewayFilterChain chain, String token, Throwable ex) {
        log.error("Auth Service not available", ex);
        exchange.getResponse().setStatusCode(HttpStatus.SERVICE_UNAVAILABLE);
        return exchange.getResponse().setComplete();
    }

}
