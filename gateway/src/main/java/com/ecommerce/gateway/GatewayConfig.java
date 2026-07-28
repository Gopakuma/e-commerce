//package com.ecommerce.gateway;
//
//import org.springframework.cloud.gateway.route.RouteLocator;
//import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class GatewayConfig {
//
//    @Bean
//    public RouteLocator customRoute(RouteLocatorBuilder routeBuilder) {
//        return routeBuilder.routes()
//                //Auth Service
//                .route("auth-service-route", r -> r
//                        .path("/auth/**")
//                        .uri("lb://auth-service"))
//
//                //User Service
//                .route("user-service-route", r -> r
//                        .path("/user/**")
//                        .uri("lb://user-service"))
//
//                .build();
//
//
//    }
//}
