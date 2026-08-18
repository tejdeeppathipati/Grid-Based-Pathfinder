package com.gridpathfinder.api;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiDocumentationConfig {
    @Bean
    OpenAPI pathfinderOpenApi() {
        return new OpenAPI().info(new Info()
                .title("Grid-Based Pathfinder API")
                .version("1.0")
                .description("Weighted-grid search, comparison, persistence, and analysis API")
                .contact(new Contact().name("Grid Pathfinder Project"))
                .license(new License().name("MIT")));
    }
}
