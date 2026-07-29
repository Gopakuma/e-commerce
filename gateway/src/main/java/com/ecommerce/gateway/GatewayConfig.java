package com.ecommerce.gateway;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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

        authServiceClient.webClient()
                .get()
                .uri("lb://auth-service/validateUser")
                .header(HttpHeaders.AUTHORIZATION, token)
                .retrieve()
                .toBodilessEntity()
                .flatMap(response -> chain.filter(exchange))
                .onErrorResume(ex -> {
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                });



        return chain.filter(exchange);
    }
}
