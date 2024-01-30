package com.r1cardoPereira.demoparkapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class SpringDocOpenApiConfig {

    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI()
        .info(
            new Info()
            .title("REST API - Spring Park")
            .description("Api para gestão de estacionamento de veículos")
            .version("v1")
            .license(new License().name("Apache 2.0").url("http://www.apache.org/licenses/LICENSE-2.0"))
            .contact(new Contact().name("Ricardo Pereira").url("https://www.linkedin.com/in/r1cardopereira/")));
    }


    
}