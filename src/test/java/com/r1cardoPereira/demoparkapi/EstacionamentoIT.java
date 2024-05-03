package com.r1cardoPereira.demoparkapi;

import com.r1cardoPereira.demoparkapi.web.dto.EstacionamentoCreateDto;
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
@Sql(scripts = "/sql/estacionamentos/estacionamentos-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/estacionamentos/estacionamentos-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)

public class EstacionamentoIT {

    @Autowired
    WebTestClient testClient;

    @Test
    public void criarCheckin_ComDadosValidos_RetornarLocationEClienteTemVagasComStatus201(){
        EstacionamentoCreateDto createDto = EstacionamentoCreateDto.builder()
                .placa("ABC-1234")
                .marca("VOLKS")
                .modelo("GOL")
                .cor("BRANCO")
                .clienteCpf("32268430014")
                .build();
        testClient
                .post()
                .uri("/api/v1/estacionamentos/check-in")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "admin@email.com",
                        "123456"))
                .bodyValue(createDto)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().exists(HttpHeaders.LOCATION)
                .expectBody()
                .jsonPath("placa").isEqualTo("ABC-1234")
                .jsonPath("marca").isEqualTo("VOLKS")
                .jsonPath("modelo").isEqualTo("GOL")
                .jsonPath("cor").isEqualTo("BRANCO")
                .jsonPath("clienteCpf").isEqualTo("32268430014")
                .jsonPath("recibo").exists()
                .jsonPath("dataEntrada").exists()
                .jsonPath("codigoVaga").exists();




    }

    @Test
    public void criarCheckin_ComCredenciaisDePerfilCliente_RetornarErrorMessageStatus403(){
        EstacionamentoCreateDto createDto = EstacionamentoCreateDto.builder()
                .placa("ABC-1234")
                .marca("VOLKS")
                .modelo("GOL")
                .cor("BRANCO")
                .clienteCpf("32268430014")
                .build();
        ErrorMessage responseBody = testClient
                .post()
                .uri("/api/v1/estacionamentos/check-in")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "client@email.com",
                        "123456"))
                .bodyValue(createDto)
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);


    }

    @Test
    public void criarCheckin_ComDadosDaPlacaInvalidos_RetornarErrorMessageStatus422(){
        EstacionamentoCreateDto createDto = EstacionamentoCreateDto.builder()
                .placa("ABC-1S34") // padrão placa AAA-0000
                .marca("VOLKS")
                .modelo("GOL")
                .cor("BRANCO")
                .clienteCpf("32268430014")
                .build();
        ErrorMessage responseBody = testClient
                .post()
                .uri("/api/v1/estacionamentos/check-in")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "admin@email.com",
                        "123456"))
                .bodyValue(createDto)
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);




    }

    @Test
    public void criarCheckin_ComDadosDaMarcaVazios_RetornarErrorMessageStatus422(){
        EstacionamentoCreateDto createDto = EstacionamentoCreateDto.builder()
                .placa("ABC-1234") // padrão placa AAA-0000
                .marca("")
                .modelo("GOL")
                .cor("BRANCO")
                .clienteCpf("32268430014")
                .build();
        ErrorMessage responseBody = testClient
                .post()
                .uri("/api/v1/estacionamentos/check-in")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "admin@email.com",
                        "123456"))
                .bodyValue(createDto)
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);




    }
    @Test
    public void criarCheckin_ComDadosDoModeloVazios_RetornarErrorMessageStatus422(){
        EstacionamentoCreateDto createDto = EstacionamentoCreateDto.builder()
                .placa("ABC-1234") // padrão placa AAA-0000
                .marca("VOLKS")
                .modelo("")
                .cor("BRANCO")
                .clienteCpf("32268430014")
                .build();
        ErrorMessage responseBody = testClient
                .post()
                .uri("/api/v1/estacionamentos/check-in")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "admin@email.com",
                        "123456"))
                .bodyValue(createDto)
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);




    }

    @Test
    public void criarCheckin_ComDadosDaCorVazios_RetornarErrorMessageStatus422(){
        EstacionamentoCreateDto createDto = EstacionamentoCreateDto.builder()
                .placa("ABC-1234") // padrão placa AAA-0000
                .marca("VOLKS")
                .modelo("GOL")
                .cor("")
                .clienteCpf("32268430014")
                .build();
        ErrorMessage responseBody = testClient
                .post()
                .uri("/api/v1/estacionamentos/check-in")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "admin@email.com",
                        "123456"))
                .bodyValue(createDto)
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);




    }

    @Test
    public void criarCheckin_ComDadosCpfFormatoInvalido_RetornarErrorMessageStatus422(){
        EstacionamentoCreateDto createDto = EstacionamentoCreateDto.builder()
                .placa("ABC-1234") // padrão placa AAA-0000
                .marca("VOLKS")
                .modelo("GOL")
                .cor("BRANCO")
                .clienteCpf("322.684.300-14")
                .build();
        ErrorMessage responseBody = testClient
                .post()
                .uri("/api/v1/estacionamentos/check-in")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "admin@email.com",
                        "123456"))
                .bodyValue(createDto)
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);




    }

    @Test
    public void criarCheckin_ComDadosCpfComDezCaracteres_RetornarErrorMessageStatus422(){
        EstacionamentoCreateDto createDto = EstacionamentoCreateDto.builder()
                .placa("ABC-1234") // padrão placa AAA-0000
                .marca("VOLKS")
                .modelo("GOL")
                .cor("BRANCO")
                .clienteCpf("3226843001")
                .build();
        ErrorMessage responseBody = testClient
                .post()
                .uri("/api/v1/estacionamentos/check-in")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "admin@email.com",
                        "123456"))
                .bodyValue(createDto)
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);




    }

    @Test
    public void criarCheckin_ComDadosCpfComDozeCaracteres_RetornarErrorMessageStatus422(){
        EstacionamentoCreateDto createDto = EstacionamentoCreateDto.builder()
                .placa("ABC-1234") // padrão placa AAA-0000
                .marca("VOLKS")
                .modelo("GOL")
                .cor("BRANCO")
                .clienteCpf("322684300142")
                .build();
        ErrorMessage responseBody = testClient
                .post()
                .uri("/api/v1/estacionamentos/check-in")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "admin@email.com",
                        "123456"))
                .bodyValue(createDto)
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);




    }

    @Test
    public void criarCheckin_ComDadosCpfVazio_RetornarErrorMessageStatus422(){
        EstacionamentoCreateDto createDto = EstacionamentoCreateDto.builder()
                .placa("ABC-1234") // padrão placa AAA-0000
                .marca("VOLKS")
                .modelo("GOL")
                .cor("BRANCO")
                .clienteCpf("")
                .build();
        ErrorMessage responseBody = testClient
                .post()
                .uri("/api/v1/estacionamentos/check-in")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "admin@email.com",
                        "123456"))
                .bodyValue(createDto)
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);




    }

    @Test
    public void criarCheckin_ComDadosCpfInvalido_RetornarErrorMessageStatus422(){
        EstacionamentoCreateDto createDto = EstacionamentoCreateDto.builder()
                .placa("ABC-1234") // padrão placa AAA-0000
                .marca("VOLKS")
                .modelo("GOL")
                .cor("BRANCO")
                .clienteCpf("02469844721")
                .build();
        ErrorMessage responseBody = testClient
                .post()
                .uri("/api/v1/estacionamentos/check-in")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "admin@email.com",
                        "123456"))
                .bodyValue(createDto)
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);




    }

    @Test
    public void criarCheckin_ComCpfNaoCadastrado_RetornarErrorMessageStatus404(){
        EstacionamentoCreateDto createDto = EstacionamentoCreateDto.builder()
                .placa("ABC-1234")
                .marca("VOLKS")
                .modelo("GOL")
                .cor("BRANCO")
                .clienteCpf("00166788007")
                .build();
        ErrorMessage responseBody = testClient
                .post()
                .uri("/api/v1/estacionamentos/check-in")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "admin@email.com",
                        "123456"))
                .bodyValue(createDto)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(404);


    }


    @Sql(scripts = "/sql/estacionamentos/estacionamentos-insert-vagas-ocupadas.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/estacionamentos/estacionamentos-delete-vagas-ocupadas.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    public void criarCheckin_ComVagasOcupadas_RetornarErrorMessageStatus404(){
        EstacionamentoCreateDto createDto = EstacionamentoCreateDto.builder()
                .placa("ABC-1234")
                .marca("VOLKS")
                .modelo("GOL")
                .cor("BRANCO")
                .clienteCpf("32268430014")
                .build();
        ErrorMessage responseBody = testClient
                .post()
                .uri("/api/v1/estacionamentos/check-in")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "admin@email.com",
                        "123456"))
                .bodyValue(createDto)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(404);


    }

    @Test
    public void buscarCheckin_ComPerfilAdmin_RetornarDadosComStatus200(){

        testClient
                .get()
                .uri("/api/v1/estacionamentos/check-in/{recibo}","20240501-095400")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "admin@email.com",
                        "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("placa").isEqualTo("FRD-1234")
                .jsonPath("marca").isEqualTo("FORD")
                .jsonPath("modelo").isEqualTo("ECOSPORT")
                .jsonPath("cor").isEqualTo("Preto")
                .jsonPath("clienteCpf").isEqualTo("32268430014")
                .jsonPath("recibo").isEqualTo("20240501-095400")
                .jsonPath("dataEntrada").isEqualTo("2024-05-01 09:54:00")
                .jsonPath("codigoVaga").isEqualTo("T-04");




    }

    @Test
    public void buscarCheckin_ComPerfilCliente_RetornarDadosComStatus200(){

        testClient
                .get()
                .uri("/api/v1/estacionamentos/check-in/{recibo}","20240501-095400")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "client@email.com",
                        "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("placa").isEqualTo("FRD-1234")
                .jsonPath("marca").isEqualTo("FORD")
                .jsonPath("modelo").isEqualTo("ECOSPORT")
                .jsonPath("cor").isEqualTo("Preto")
                .jsonPath("clienteCpf").isEqualTo("32268430014")
                .jsonPath("recibo").isEqualTo("20240501-095400")
                .jsonPath("dataEntrada").isEqualTo("2024-05-01 09:54:00")
                .jsonPath("codigoVaga").isEqualTo("T-04");




    }

    @Test
    public void buscarCheckin_ComReciboInexistente_RetornarErrorComStatus404(){

        testClient
                .get()
                .uri("/api/v1/estacionamentos/check-in/{recibo}","20240501-095499")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "client@email.com",
                        "123456"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("status").isEqualTo("404")
                .jsonPath("path").isEqualTo("/api/v1/estacionamentos/check-in/20240501-095499")
                .jsonPath("method").isEqualTo("GET");
    }

    @Test
    public void criarCheckOut_ComReciboExistente_RetornarSucesso200(){

        testClient
                .put()
                .uri("/api/v1/estacionamentos/check-out/{recibo}","20240501-095400")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "admin@email.com",
                        "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("placa").isEqualTo("FRD-1234")
                .jsonPath("marca").isEqualTo("FORD")
                .jsonPath("modelo").isEqualTo("ECOSPORT")
                .jsonPath("cor").isEqualTo("Preto")
                .jsonPath("clienteCpf").isEqualTo("32268430014")
                .jsonPath("recibo").isEqualTo("20240501-095400")
                .jsonPath("dataEntrada").isEqualTo("2024-05-01 09:54:00")
                .jsonPath("codigoVaga").isEqualTo("T-04")
                .jsonPath("dataSaida").exists()
                .jsonPath("valor").exists()
                .jsonPath("desconto").exists();
    }

    @Test
    public void criarCheckout_ComReciboInexistente_RetornarErrorComStatus404(){

        testClient
                .put()
                .uri("/api/v1/estacionamentos/check-out/{recibo}","20240501-095499")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "admin@email.com",
                        "123456"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("status").isEqualTo("404")
                .jsonPath("path").isEqualTo("/api/v1/estacionamentos/check-out/20240501-095499")
                .jsonPath("method").isEqualTo("PUT");
    }

    @Test
    public void criarCheckout_ComCredenciaisDeCliente_RetornarErrorComStatus403(){

        testClient
                .put()
                .uri("/api/v1/estacionamentos/check-out/{recibo}","20240501-095400")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "client@email.com",
                        "123456"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody()
                .jsonPath("status").isEqualTo("403")
                .jsonPath("path").isEqualTo("/api/v1/estacionamentos/check-out/20240501-095400")
                .jsonPath("method").isEqualTo("PUT");
    }





}
