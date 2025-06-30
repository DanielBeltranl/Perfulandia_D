package com.example.perfulandia.Config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI(){

        return new OpenAPI()
                .info(new Info()
                .title("Perfulandia API")
                        .version("1.0")
                        .description("Documentacacion del sistema de creacion de datos de pago"));

    }
}
