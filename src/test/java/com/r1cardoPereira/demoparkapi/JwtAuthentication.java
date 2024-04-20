package com.r1cardoPereira.demoparkapi;

import java.util.function.Consumer;

import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.r1cardoPereira.demoparkapi.jwt.JwtToken;
import com.r1cardoPereira.demoparkapi.web.dto.UsuarioLoginDto;

public class JwtAuthentication {
    
    public static Consumer<HttpHeaders> getHeaderAuthorization(WebTestClient client, String username,String password) {

        String token = client
                        .post()
                        .uri("/api/v1/auth")
                        .bodyValue(new UsuarioLoginDto(username, password))
                        .exchange()
                        .expectStatus().isOk()
                        .expectBody(JwtToken.class)
                        .returnResult().getResponseBody().getToken();

        return headers -> headers.add(HttpHeaders.AUTHORIZATION,"Bearer " + token);


    }
}
