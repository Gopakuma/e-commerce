package com.ecommerce.gateway;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.properties.AbstractSwaggerUiConfigProperties;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.HashSet;
import java.util.Set;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Gateway Service",
                description = "Microservice API Gateway"
        )
)
public class SwaggerAggregationConfig {


    @Bean
    @Primary
    public SwaggerUiConfigProperties swaggerUiConfigProperties() {

        SwaggerUiConfigProperties config = new SwaggerUiConfigProperties();

        Set<AbstractSwaggerUiConfigProperties.SwaggerUrl> urls = new HashSet<>();


        // Gateway API docs
        AbstractSwaggerUiConfigProperties.SwaggerUrl gateway =
                new AbstractSwaggerUiConfigProperties.SwaggerUrl();

        gateway.setName("gateway-service");
        gateway.setUrl("/v3/api-docs");

        urls.add(gateway);


        // Auth Service API docs
        AbstractSwaggerUiConfigProperties.SwaggerUrl authService =
                new AbstractSwaggerUiConfigProperties.SwaggerUrl();

        authService.setName("auth-service");
        authService.setUrl("/auth/v3/api-docs");

        urls.add(authService);


        // User Service API docs (add when user-service is ready)
        AbstractSwaggerUiConfigProperties.SwaggerUrl userService =
                new AbstractSwaggerUiConfigProperties.SwaggerUrl();

        userService.setName("user-service");
        userService.setUrl("/users/v3/api-docs");

        urls.add(userService);


        config.setUrls(urls);

        return config;
    }
}