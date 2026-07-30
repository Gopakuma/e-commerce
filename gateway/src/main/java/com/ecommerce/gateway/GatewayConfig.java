package com.ecommerce.gateway;

import com.ecommerce.gateway.dto.ValidateJwtResponse;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class GatewayConfig implements GlobalFilter {

    private final AuthServiceWebClient authServiceClient;

    public GatewayConfig(AuthServiceWebClient authServiceClient) {
        this.authServiceClient = authServiceClient;
    }


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();

        if (path.equals("/auth/login")
                || path.equals("/auth/register")
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

        return authServiceClient.webClient()
                .get()
                .uri("lb://auth-service/validateUser")
                .header(HttpHeaders.AUTHORIZATION, token)
                .retrieve()
                .bodyToMono(ValidateJwtResponse.class)
                .flatMap(response ->{
                    ServerHttpRequest request = exchange.getRequest().mutate()
                            .header("X-User-Id", response.getUuid())
                            .header("X-User-Email", response.getEmail())
                            .header("X-Username", response.getUsername())
                            .build();
                 return chain.filter(exchange.mutate().request(request).build());
                })
                .onErrorResume(ex -> {
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                });
    }
}
