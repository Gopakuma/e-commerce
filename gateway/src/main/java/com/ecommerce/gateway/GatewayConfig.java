package com.ecommerce.gateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRoute(RouteLocatorBuilder routeBuilder) {
        return routeBuilder.routes()
                //Auth Service
                .route("auth-service-route", r -> r
                        .path("/auth/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://auth-service"))

                //Swagger
                .route("auth-openapi-route", r -> r
                        .path("/v3/api-docs/**")
                        .uri("lb://auth-service"))
                .build();
    }
}
