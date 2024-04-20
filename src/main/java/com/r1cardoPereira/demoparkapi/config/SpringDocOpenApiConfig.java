package com.r1cardoPereira.demoparkapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;

// A anotação @Configuration indica que esta classe é uma classe de configuração do Spring.
@Configuration
public class SpringDocOpenApiConfig {

    // A anotação @Bean indica que o método a seguir é um bean do Spring, ou seja, um objeto que será gerenciado pelo container do Spring.
    @Bean
    public OpenAPI openAPI(){
        // Cria uma nova instância de OpenAPI.
        return new OpenAPI()
        .components(new Components().addSecuritySchemes("Security", securityScheme()))
        // Define as informações da API.
        .info(
            // Cria uma nova instância de Info.
            new Info()
            // Define o título da API.
            .title("REST API - Spring Park")
            // Define a descrição da API.
            .description("Api para gestão de estacionamento de veículos")
            // Define a versão da API.
            .version("v1")
            // Define a licença da API.
            .license(new License().name("Apache 2.0").url("http://www.apache.org/licenses/LICENSE-2.0"))
            // Define o contato da API.
            .contact(new Contact().name("Ricardo Pereira").url("https://www.linkedin.com/in/r1cardopereira/")));
    }

    private SecurityScheme securityScheme() {
        return new SecurityScheme()
            .description("Insira um Bearer token valido para prosseguir")
            .type(SecurityScheme.Type.HTTP)
            .in(SecurityScheme.In.HEADER)
            .scheme("bearer")
            .bearerFormat("JWT")
            .name("Security");
    }
}
