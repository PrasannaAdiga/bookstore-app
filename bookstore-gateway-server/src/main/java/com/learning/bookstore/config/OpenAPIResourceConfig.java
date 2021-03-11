package com.learning.bookstore.config;

import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.SwaggerUiConfigParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class OpenAPIResourceConfig {
    @Autowired
    RouteDefinitionLocator locator;

    @Bean
    public List<GroupedOpenApi> apis(SwaggerUiConfigParameters swaggerUiConfigParameters, RouteDefinitionLocator locator) {
        List<GroupedOpenApi> groups = new ArrayList<>();
        List<RouteDefinition> definitions = locator.getRouteDefinitions().collectList().block();
        definitions.stream().filter(routeDefinition -> routeDefinition.getId().matches(".*-service") &&
                !routeDefinition.getId().contains("DiscoveryClient")).forEach(routeDefinition -> {
            String name = routeDefinition.getId().replaceAll("-service", "");
            swaggerUiConfigParameters.addGroup(name);
            groups.add(GroupedOpenApi.builder().pathsToMatch("/" + name + "/**").group(name).build());
        });
        return groups;
    }

}
