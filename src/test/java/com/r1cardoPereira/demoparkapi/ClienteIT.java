package com.r1cardoPereira.demoparkapi;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.r1cardoPereira.demoparkapi.web.dto.ClienteCreateDto;
import com.r1cardoPereira.demoparkapi.web.dto.ClienteResponseDto;
import com.r1cardoPereira.demoparkapi.web.dto.PageableDto;
import com.r1cardoPereira.demoparkapi.web.exception.ErrorMessage;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/clientes/clientes-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/clientes/clientes-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ClienteIT {

        @Autowired
        WebTestClient testClient;

        @Test
        public void criarCliente_ComNomeESenhaValidos_RetornarClienteCriadoStatus201() {

                ClienteResponseDto responseBody = testClient
                                .post()
                                .uri("/api/v1/clientes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "cliente4@email.com",
                                                "123456"))
                                .bodyValue(new ClienteCreateDto("Cliente Cinco", "60505953056"))
                                .exchange()
                                .expectStatus().isCreated()
                                .expectBody(ClienteResponseDto.class)
                                .returnResult().getResponseBody();

                Assertions.assertThat(responseBody).isNotNull();
                Assertions.assertThat(responseBody.getId()).isNotNull();
                Assertions.assertThat(responseBody.getNome()).isEqualTo("Cliente Cinco");
                Assertions.assertThat(responseBody.getCpf()).isEqualTo("60505953056");

        }

        @Test
        public void criarCliente_ComCpfJaCadastrado_RetornarErrorMessageStatus409() {

                ErrorMessage responseBody = testClient
                                .post()
                                .uri("/api/v1/clientes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "client@email.com",
                                                "123456"))
                                .bodyValue(new ClienteCreateDto("Cliente Dois", "25811504080"))
                                .exchange()
                                .expectStatus().isEqualTo(409)
                                .expectBody(ErrorMessage.class)
                                .returnResult().getResponseBody();

                Assertions.assertThat(responseBody).isNotNull();
                Assertions.assertThat(responseBody.getStatus()).isEqualTo(409);

        }

        @Test
        public void criarCliente_ComUsuarioNaoPermitido_RetornarErrorMessageStatus403() {

                ErrorMessage responseBody = testClient
                                .post()
                                .uri("/api/v1/clientes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "admin@email.com",
                                                "123456"))
                                .bodyValue(new ClienteCreateDto("Cliente Dois", "25811504080"))
                                .exchange()
                                .expectStatus().isEqualTo(403)
                                .expectBody(ErrorMessage.class)
                                .returnResult().getResponseBody();

                Assertions.assertThat(responseBody).isNotNull();
                Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);

        }

        @Test
        public void criarCliente_ComNomeInvalido_RetornarErrorMessageStatus422() {

                ErrorMessage responseBody = testClient
                                .post()
                                .uri("/api/v1/clientes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "cliente4@email.com",
                                                "123456"))
                                .bodyValue(new ClienteCreateDto("Clie", "60505953056"))
                                .exchange()
                                .expectStatus().isEqualTo(422)
                                .expectBody(ErrorMessage.class)
                                .returnResult().getResponseBody();

                Assertions.assertThat(responseBody).isNotNull();
                Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

                responseBody = testClient
                                .post()
                                .uri("/api/v1/clientes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "cliente4@email.com",
                                                "123456"))
                                .bodyValue(new ClienteCreateDto("  ", "60505953056"))
                                .exchange()
                                .expectStatus().isEqualTo(422)
                                .expectBody(ErrorMessage.class)
                                .returnResult().getResponseBody();

                Assertions.assertThat(responseBody).isNotNull();
                Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        }

        @Test
        public void criarCliente_ComCpfInvalido_RetornarErrorMessageStatus422() {

                ErrorMessage responseBody = testClient
                                .post()
                                .uri("/api/v1/clientes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "cliente4@email.com",
                                                "123456"))
                                .bodyValue(new ClienteCreateDto("Cliente Cinco", "6050595305"))
                                .exchange()
                                .expectStatus().isEqualTo(422)
                                .expectBody(ErrorMessage.class)
                                .returnResult().getResponseBody();

                Assertions.assertThat(responseBody).isNotNull();
                Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

                responseBody = testClient
                                .post()
                                .uri("/api/v1/clientes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "cliente4@email.com",
                                                "123456"))
                                .bodyValue(new ClienteCreateDto("Cliente Cinco", "605059530566"))
                                .exchange()
                                .expectStatus().isEqualTo(422)
                                .expectBody(ErrorMessage.class)
                                .returnResult().getResponseBody();

                Assertions.assertThat(responseBody).isNotNull();
                Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

                responseBody = testClient
                                .post()
                                .uri("/api/v1/clientes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "cliente4@email.com",
                                                "123456"))
                                .bodyValue(new ClienteCreateDto("Cliente Cinco", "605.059.530-56"))
                                .exchange()
                                .expectStatus().isEqualTo(422)
                                .expectBody(ErrorMessage.class)
                                .returnResult().getResponseBody();

                Assertions.assertThat(responseBody).isNotNull();
                Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        }

        @Test
        public void buscarCLiente_ComIdExistente_RetornarCLienteComStatus200() {

                ClienteResponseDto responseBody = testClient
                                .get()
                                .uri("/api/v1/clientes/10")
                                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "admin@email.com",
                                                "123456"))
                                .exchange()
                                .expectStatus().isOk()
                                .expectBody(ClienteResponseDto.class)
                                .returnResult().getResponseBody();

                Assertions.assertThat(responseBody).isNotNull();
                Assertions.assertThat(responseBody.getId()).isEqualTo(10);
                Assertions.assertThat(responseBody.getNome()).isEqualTo("Cliente um");
                Assertions.assertThat(responseBody.getCpf()).isEqualTo("32268430014");

        }

        @Test
        public void buscarCLiente_ComIdInexistentePeloAdmin_RetornarErrorMessageComStatus404() {

                ErrorMessage responseBody = testClient
                                .get()
                                .uri("/api/v1/clientes/13")
                                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "admin@email.com",
                                                "123456"))
                                .exchange()
                                .expectStatus().isNotFound()
                                .expectBody(ErrorMessage.class)
                                .returnResult().getResponseBody();

                Assertions.assertThat(responseBody).isNotNull();
                Assertions.assertThat(responseBody.getStatus()).isEqualTo(404);

        }

        @Test

        public void buscarCLiente_ComClienteNaoPermitido_RetornarErrorMessageComStatus403() {

                ErrorMessage responseBody = testClient
                                .get()
                                .uri("/api/v1/clientes/10")
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
        public void buscarClientes_ListarClientesComPaginacaoPeloAdmin_RetornarClientesComStatus200(){
                
                PageableDto responseBody = testClient
                                .get()
                                .uri("api/v1/clientes")
                                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "admin@email.com",
                                                "123456"))
                                .exchange()
                                .expectStatus().isOk()
                                .expectBody(PageableDto.class)
                                .returnResult().getResponseBody();

                Assertions.assertThat(responseBody).isNotNull();
                Assertions.assertThat(responseBody.getTotalElements()).isEqualTo(2);
                Assertions.assertThat(responseBody.getNumber()).isEqualTo(0);

                responseBody = testClient
                                .get()
                                .uri("api/v1/clientes")
                                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "admin@email.com",
                                                "123456"))
                                .exchange()
                                .expectStatus().isOk()
                                .expectBody(PageableDto.class)
                                .returnResult().getResponseBody();

                Assertions.assertThat(responseBody).isNotNull();
                Assertions.assertThat(responseBody.getContent().size()).isEqualTo(2);
                Assertions.assertThat(responseBody.getTotalElements()).isEqualTo(2);
                Assertions.assertThat(responseBody.getNumber()).isEqualTo(0);
        }

        @Test
        public void buscarClientes_ListarClientesComPerfilCliente_RetornarErrorMessageComStatus403(){
                
                ErrorMessage responseBody = testClient
                                .get()
                                .uri("api/v1/clientes")
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
        public void recuperarDadosDeCLienteAutenticado_RetornarClienteComStatus200(){

                ClienteResponseDto responsebody = testClient
                        .get()
                        .uri("api/v1/clientes/detalhes")
                        .headers(JwtAuthentication.getHeaderAuthorization(testClient, "client@email.com", "123456"))
                        .exchange()
                        .expectStatus().isOk()
                        .expectBody(ClienteResponseDto.class)
                        .returnResult().getResponseBody();

                Assertions.assertThat(responsebody).isNotNull();
                Assertions.assertThat(responsebody.getNome()).isEqualTo("Cliente um");
                Assertions.assertThat(responsebody.getCpf()).isEqualTo("32268430014");
                Assertions.assertThat(responsebody.getId()).isEqualTo(10);

        }

        @Test
        public void recuperarDadosDeClienteEmUrlDetalhesComPerfilAdmin_RetornarErrorMessageComStatus403(){

                ErrorMessage responsebody = testClient
                        .get()
                        .uri("api/v1/clientes/detalhes")
                        .headers(JwtAuthentication.getHeaderAuthorization(testClient, "admin@email.com", "123456"))
                        .exchange()
                        .expectStatus().isForbidden()
                        .expectBody(ErrorMessage.class)
                        .returnResult().getResponseBody();

                Assertions.assertThat(responsebody).isNotNull();
                Assertions.assertThat(responsebody.getStatus()).isEqualTo(403);

        }
}


