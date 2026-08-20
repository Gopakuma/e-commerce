package com.ecommerce.gateway;

import com.ecommerce.gateway.dto.ValidateJwtResponse;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class WebClientServices {

    private static WebClient serviceClient;

    private WebClientServices(WebClient.Builder builder) {
        this.serviceClient = builder.build();
    }

    public static Mono<Void> authService(ServerWebExchange exchange, GatewayFilterChain chain, String token) {
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
                    ex.printStackTrace();
                    return exchange.getResponse().setComplete();
                });
         }

}
