package com.r1cardoPereira.demoparkapi;

import com.r1cardoPereira.demoparkapi.web.dto.VagaCreateDto;
import com.r1cardoPereira.demoparkapi.web.exception.ErrorMessage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/vagas/vagas-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/vagas/vagas-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)

public class VagaIT {

    @Autowired
    WebTestClient testClient;

    @Test
    public  void criarVaga_comDadosValidos_RetornarLocationStatus201(){

        testClient
                .post()
                .uri("/api/v1/vagas")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "admin@email.com",
                        "123456"))
                .bodyValue(new VagaCreateDto("V-02", "LIVRE"))
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().exists(HttpHeaders.LOCATION);

    }


    @Test
    public  void criarVaga_comDadosDeUsuarioPerfilCliente_RetornarErroMessageStatus403(){

        ErrorMessage responseBody = testClient
                .post()
                .uri("/api/v1/vagas")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "client@email.com",
                        "123456"))
                .bodyValue(new VagaCreateDto("V-02", "LIVRE"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);



    }

    @Test
    public  void criarVaga_comCodigoJaExistentes_RetornarErrorMessageStatus409(){

        ErrorMessage responsebody = testClient
                .post()
                .uri("/api/v1/vagas")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "admin@email.com",
                        "123456"))
                .bodyValue(new VagaCreateDto("V-01", "LIVRE"))
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responsebody).isNotNull();
        Assertions.assertThat(responsebody.getStatus()).isEqualTo(409);

    }

    @Test
    public  void criarVaga_comDadosInvalidos_RetornarErrorMessageStatus422(){

        ErrorMessage responsebody = testClient
                .post()
                .uri("/api/v1/vagas")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "admin@email.com",
                        "123456"))
                .bodyValue(new VagaCreateDto("", ""))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responsebody).isNotNull();
        Assertions.assertThat(responsebody.getStatus()).isEqualTo(422);

        responsebody = testClient
                .post()
                .uri("/api/v1/vagas")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "admin@email.com",
                        "123456"))
                .bodyValue(new VagaCreateDto("V-022", "LIVR"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responsebody).isNotNull();
        Assertions.assertThat(responsebody.getStatus()).isEqualTo(422);

        responsebody = testClient
                .post()
                .uri("/api/v1/vagas")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "admin@email.com",
                        "123456"))
                .bodyValue(new VagaCreateDto("V-02", "LIVREE"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responsebody).isNotNull();
        Assertions.assertThat(responsebody.getStatus()).isEqualTo(422);

    }

    @Test
    public void buscarVaga_ComCodigoValido_RetornarVagaComStatus200(){



        testClient
                .get()
                .uri("/api/v1/vagas/{codigo}","V-01")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "admin@email.com",
                                                                                "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("id").isEqualTo(10)
                .jsonPath("codigo").isEqualTo("V-01")
                .jsonPath("status").isEqualTo("LIVRE");




    }
    @Test
    public  void buscarVaga_comDadosDeUsuarioPerfilCliente_RetornarErroMessageStatus403(){

        testClient
                .get()
                .uri("/api/v1/vagas/{codigo}","V-01")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "client@email.com",
                        "123456"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody()
                .jsonPath("status").isEqualTo(403)
                .jsonPath("method").isEqualTo("GET")
                .jsonPath("path").isEqualTo("/api/v1/vagas/V-01");

        ErrorMessage responseBody = testClient
                .get()
                .uri("/api/v1/vagas/{codigo}","V-01")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "client@email.com",
                        "123456"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);



    }

    @Test
    public void buscarVaga_ComCodigoInvalido_RetornarErrorMessageComStatus404(){


        testClient
                .get()
                .uri("/api/v1/vagas/{codigo}","V-02")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "admin@email.com",
                        "123456"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("status").isEqualTo(404)
                .jsonPath("method").isEqualTo("GET")
                .jsonPath("path").isEqualTo("/api/v1/vagas/V-02");




        ErrorMessage responsebody = testClient
                .get()
                .uri("/api/v1/vagas/{codigo}","V-02")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "admin@email.com",
                        "123456"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responsebody).isNotNull();
        Assertions.assertThat(responsebody.getStatus()).isEqualTo(404);





    }

}
