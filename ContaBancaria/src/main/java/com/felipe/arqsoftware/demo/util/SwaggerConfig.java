package com.felipe.arqsoftware.demo.util;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API - BANCO TAP")
                        .version("1.0.0")
                        .description("Documentação da API do BANCO TAP")
                        .contact(new Contact()
                                .name("Suporte")
                                .email("suporte@contabancaria.com")));
    }
}

