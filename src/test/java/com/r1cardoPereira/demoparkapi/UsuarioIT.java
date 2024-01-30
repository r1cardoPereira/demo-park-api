package com.r1cardoPereira.demoparkapi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import org.assertj.core.api.Assertions;

import com.r1cardoPereira.demoparkapi.web.dto.UsuarioCreateDto;
import com.r1cardoPereira.demoparkapi.web.dto.UsuarioResponseDto;
import com.r1cardoPereira.demoparkapi.web.dto.UsuarioSenhaDto;
import com.r1cardoPereira.demoparkapi.web.dto.mapper.UsuarioMapper;
import com.r1cardoPereira.demoparkapi.web.exception.ErrorMessage;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/usuarios/usuarios-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/usuarios/usuarios-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UsuarioIT {

        @Autowired
        WebTestClient testClient;

        @Test
        public void criarUsuario_ComUsernameESenhaValidos_RetornarUsuarioCriadoStatus201() {

                UsuarioResponseDto responseBody = testClient
                                .post()
                                .uri("/api/v1/usuarios")
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(new UsuarioCreateDto("username11@gmail.com", "123456"))
                                .exchange()
                                .expectStatus().isCreated()
                                .expectBody(UsuarioResponseDto.class)
                                .returnResult().getResponseBody();

                Assertions.assertThat(responseBody).isNotNull();
                Assertions.assertThat(responseBody.getId()).isNotNull();
                Assertions.assertThat(responseBody.getUsername()).isEqualTo("username11@gmail.com");
                Assertions.assertThat(responseBody.getRole()).isEqualTo("CLIENTE");

        }

        @Test
        public void criarUsuario_ComUsernameInvalido_RetornarErrorMessageStatus422() {

                ErrorMessage responseBody = testClient
                                .post()
                                .uri("/api/v1/usuarios")
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(new UsuarioCreateDto("", "123456"))
                                .exchange()
                                .expectStatus().isEqualTo(422)
                                .expectBody(ErrorMessage.class)
                                .returnResult().getResponseBody();

                Assertions.assertThat(responseBody).isNotNull();
                Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

                responseBody = testClient
                                .post()
                                .uri("/api/v1/usuarios")
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(new UsuarioCreateDto("binho@", "123456"))
                                .exchange()
                                .expectStatus().isEqualTo(422)
                                .expectBody(ErrorMessage.class)
                                .returnResult().getResponseBody();

                Assertions.assertThat(responseBody).isNotNull();
                Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

                responseBody = testClient
                                .post()
                                .uri("/api/v1/usuarios")
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(new UsuarioCreateDto("binho@gmeisl", "123456"))
                                .exchange()
                                .expectStatus().isEqualTo(422)
                                .expectBody(ErrorMessage.class)
                                .returnResult().getResponseBody();

                Assertions.assertThat(responseBody).isNotNull();
                Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

                responseBody = testClient
                                .post()
                                .uri("/api/v1/usuarios")
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(new UsuarioCreateDto("binho@gmeisl.", "123456"))
                                .exchange()
                                .expectStatus().isEqualTo(422)
                                .expectBody(ErrorMessage.class)
                                .returnResult().getResponseBody();

                Assertions.assertThat(responseBody).isNotNull();
                Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        }

        @Test
        public void criarUsuario_ComPasswordInvalido_RetornarErrorMessageStatus422() {

                ErrorMessage responseBody = testClient
                                .post()
                                .uri("/api/v1/usuarios")
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(new UsuarioCreateDto("username@teste.com", ""))
                                .exchange()
                                .expectStatus().isEqualTo(422)
                                .expectBody(ErrorMessage.class)
                                .returnResult().getResponseBody();

                Assertions.assertThat(responseBody).isNotNull();
                Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

                responseBody = testClient
                                .post()
                                .uri("/api/v1/usuarios")
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(new UsuarioCreateDto("username@teste.com", "12346"))
                                .exchange()
                                .expectStatus().isEqualTo(422)
                                .expectBody(ErrorMessage.class)
                                .returnResult().getResponseBody();

                Assertions.assertThat(responseBody).isNotNull();
                Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

                responseBody = testClient
                                .post()
                                .uri("/api/v1/usuarios")
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(new UsuarioCreateDto("username@teste.com", "1234567"))
                                .exchange()
                                .expectStatus().isEqualTo(422)
                                .expectBody(ErrorMessage.class)
                                .returnResult().getResponseBody();

        }

        @Test
        public void criarUsuario_ComUsernameRepetido_RetornarErrorMessageComStatus409() {

                ErrorMessage responseBody = testClient
                                .post()
                                .uri("/api/v1/usuarios")
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(new UsuarioCreateDto("client@email.com", "123456"))
                                .exchange()
                                .expectStatus().isEqualTo(409)
                                .expectBody(ErrorMessage.class)
                                .returnResult().getResponseBody();

                Assertions.assertThat(responseBody).isNotNull();
                Assertions.assertThat(responseBody.getStatus()).isEqualTo(409);

        }

        @Test
        public void buscarUsuario_ComIdExistente_RetornarUsuarioComStatus200() {

                UsuarioResponseDto responseBody = testClient
                                .get()
                                .uri("/api/v1/usuarios/101")
                                .exchange()
                                .expectStatus().isOk()
                                .expectBody(UsuarioResponseDto.class)
                                .returnResult().getResponseBody();

                Assertions.assertThat(responseBody).isNotNull();
                Assertions.assertThat(responseBody.getId()).isEqualTo(101);
                Assertions.assertThat(responseBody.getUsername()).isEqualTo("admin@email.com");
                Assertions.assertThat(responseBody.getRole()).isEqualTo("ADMIN");

        }

        @Test
        public void buscarUsuario_ComIdInexistente_ErrorMessageComStatus404() {

                ErrorMessage responseBody = testClient
                                .get()
                                .uri("/api/v1/usuarios/120")
                                .exchange()
                                .expectStatus().isNotFound()
                                .expectBody(ErrorMessage.class)
                                .returnResult().getResponseBody();

                Assertions.assertThat(responseBody).isNotNull();
                Assertions.assertThat(responseBody.getStatus()).isEqualTo(404);

        }

        @Test
        public void editarSenha_ComDadosValidos_RetornarSucessoEStatusCode204() {

                testClient
                                .patch()
                                .uri("/api/v1/usuarios/102")
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(new UsuarioSenhaDto("123456", "000000", "000000"))
                                .exchange()
                                .expectStatus().isNoContent();

        }

        @Test
        public void editarSenha_ComIdInexistente_ErrorMessageComStatus404() {

                ErrorMessage responseBody = testClient
                                .patch()
                                .uri("/api/v1/usuarios/120")
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(new UsuarioSenhaDto("123456", "000000", "000000"))
                                .exchange()
                                .expectStatus().isNotFound()
                                .expectBody(ErrorMessage.class)
                                .returnResult().getResponseBody();

                Assertions.assertThat(responseBody).isNotNull();
                Assertions.assertThat(responseBody.getStatus()).isEqualTo(404);

        }

        @Test 
        public void editarSenha_ComCamposInvalidos_RetornarErrorMessageComStatus422() {
                
                ErrorMessage responseBody = testClient
                        .patch()
                        .uri("api/v1/usuarios/102")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(new UsuarioSenhaDto("", "", ""))
                        .exchange()
                        .expectStatus().isEqualTo(422)
                        .expectBody(ErrorMessage.class)
                        .returnResult().getResponseBody();

                Assertions.assertThat(responseBody).isNotNull();
                Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

                responseBody = testClient
                        .patch()
                        .uri("api/v1/usuarios/102")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(new UsuarioSenhaDto("12345", "00000", "00000"))
                        .exchange()
                        .expectStatus().isEqualTo(422)
                        .expectBody(ErrorMessage.class)
                        .returnResult().getResponseBody();

                Assertions.assertThat(responseBody).isNotNull();
                Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

                responseBody = testClient
                        .patch()
                        .uri("api/v1/usuarios/102")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(new UsuarioSenhaDto("1234567", "000000", "000000"))
                        .exchange()
                        .expectStatus().isEqualTo(422)
                        .expectBody(ErrorMessage.class)
                        .returnResult().getResponseBody();

                Assertions.assertThat(responseBody).isNotNull();
                Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

                responseBody = testClient
                        .patch()
                        .uri("api/v1/usuarios/102")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(new UsuarioSenhaDto("123456", "0000000", "0000000"))
                        .exchange()
                        .expectStatus().isEqualTo(422)
                        .expectBody(ErrorMessage.class)
                        .returnResult().getResponseBody();

                Assertions.assertThat(responseBody).isNotNull();
                Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
            
                

        }
        @Test 
        public void editarSenha_ComSenhasInvalidos_RetornarErrorMessageComStatus400() {
                
                ErrorMessage responseBody = testClient
                        .patch()
                        .uri("api/v1/usuarios/102")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(new UsuarioSenhaDto("123456", "123456", "000000"))
                        .exchange()
                        .expectStatus().isEqualTo(400)
                        .expectBody(ErrorMessage.class)
                        .returnResult().getResponseBody();

                Assertions.assertThat(responseBody).isNotNull();
                Assertions.assertThat(responseBody.getStatus()).isEqualTo(400);

                responseBody = testClient
                        .patch()
                        .uri("api/v1/usuarios/102")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(new UsuarioSenhaDto("000000", "123456", "123456"))
                        .exchange()
                        .expectStatus().isEqualTo(400)
                        .expectBody(ErrorMessage.class)
                        .returnResult().getResponseBody();

                Assertions.assertThat(responseBody).isNotNull();
                Assertions.assertThat(responseBody.getStatus()).isEqualTo(400);

                

                
                
        }

        @Test
        public void buscarUsuarios_ListarUsuarios_RetornarListaDeUsuariosComStatus200() {

                List<UsuarioResponseDto> responseBody = testClient
                        .get()
                        .uri("api/v1/usuarios")
                        .exchange()
                        .expectStatus().isOk()
                        .expectBodyList(UsuarioResponseDto.class)
                        .returnResult().getResponseBody();
                
                Assertions.assertThat(responseBody).isNotNull();
                Assertions.assertThat(responseBody.size()).isEqualTo(5);
                

        
        }
}
