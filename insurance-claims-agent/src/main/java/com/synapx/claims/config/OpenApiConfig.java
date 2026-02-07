package com.synapx.claims.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Insurance Claims Processing Agent API")
                        .version("1.0.0")
                        .description("Autonomous agent for processing First Notice of Loss (FNOL) documents. " +
                                   "Extracts key fields, validates data, and routes claims based on business rules.")
                        .contact(new Contact()
                                .name("Synapx")
                                .email("support@synapx.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
}
