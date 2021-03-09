package com.learning.bookstore.infrastructure.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class OpenAPIDocsConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        List servers = new ArrayList<Server>();
        servers.add(new Server().url("http://localhost:8080/bookstore-payment-service").description("Development server"));

        return new OpenAPI().components(new Components()).info(new Info()
                .description("<p>Provides list of REST APIs for Payment Service</p>")
                .title("API documentation for Payment Service").version("1.0.0")).servers(servers);
    }
}
