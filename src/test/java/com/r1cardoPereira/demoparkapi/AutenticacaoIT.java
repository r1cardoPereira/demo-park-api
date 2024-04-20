package com.r1cardoPereira.demoparkapi;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.r1cardoPereira.demoparkapi.jwt.JwtToken;
import com.r1cardoPereira.demoparkapi.web.dto.UsuarioLoginDto;
import com.r1cardoPereira.demoparkapi.web.exception.ErrorMessage;



@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/usuarios/usuarios-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/usuarios/usuarios-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class AutenticacaoIT {

    @Autowired
    WebTestClient testClient;

    @Test
    public void autenticar_ComCredenciaisValidas_RetornarTokenComStatus200() {

        JwtToken responseBody = testClient

                .post()
                .uri("/api/v1/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioLoginDto("admin@email.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(JwtToken.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        

    }

    @Test
    public void autenticar_ComCredenciaisInvalidas_RetornarErrorMessageComStatus400() {

        ErrorMessage responseBody = testClient

                .post()
                .uri("/api/v1/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioLoginDto("client000@email.com", "123456"))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(400);

        responseBody = testClient

                .post()
                .uri("/api/v1/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioLoginDto("admin@email.com", "123457"))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(400);
        

    }

    @Test
    public void autenticar_ComUsernameInvalido_RetornarErrorMessageComStatus422() {

        ErrorMessage responseBody = testClient

                .post()
                .uri("/api/v1/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioLoginDto("", "123456"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
        
        responseBody = testClient

                .post()
                .uri("/api/v1/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioLoginDto("@email.com", "123456"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient

                .post()
                .uri("/api/v1/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioLoginDto("admin@email", "123456"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        }
    
    @Test
    public void autenticar_ComPasswordInvalido_RetornarErrorMessageComStatus422() {
    
            ErrorMessage responseBody = testClient
    
                    .post()
                    .uri("/api/v1/auth")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(new UsuarioLoginDto("admin@email.com", ""))
                    .exchange()
                    .expectStatus().isEqualTo(422)
                    .expectBody(ErrorMessage.class)
                    .returnResult().getResponseBody();
    
            Assertions.assertThat(responseBody).isNotNull();
            Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
            
            responseBody = testClient
    
                    .post()
                    .uri("/api/v1/auth")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(new UsuarioLoginDto("admin@email.com", "1234567"))
                    .exchange()
                    .expectStatus().isEqualTo(422)
                    .expectBody(ErrorMessage.class)
                    .returnResult().getResponseBody();
    
            Assertions.assertThat(responseBody).isNotNull();
            Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
    
            responseBody = testClient
    
                    .post()
                    .uri("/api/v1/auth")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(new UsuarioLoginDto("admin@email.com", "12345"))
                    .exchange()
                    .expectStatus().isEqualTo(422)
                    .expectBody(ErrorMessage.class)
                    .returnResult().getResponseBody();
    
            Assertions.assertThat(responseBody).isNotNull();
            Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
    
            }
}
