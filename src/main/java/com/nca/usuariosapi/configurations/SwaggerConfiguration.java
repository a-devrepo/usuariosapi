package com.nca.usuariosapi.configurations;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Usuarios API")
                        .description("API Spring Boot com Spring Data JPA e JWT para gerenciamento de usuários")
                        .version("v1"));
    }
}