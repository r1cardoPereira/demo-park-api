package com.r1cardoPereira.demoparkapi.jwt;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtUtils {

    // Constantes usadas na criação e validação do token JWT.
    public static final String JWT_BEARER = "Bearer "; // Prefixo do token JWT.
    public static final String JTW_AUTHORIZATION = "Authorization"; // Cabeçalho HTTP onde o token JWT é enviado.
    public static final String SECRET_KEY = "256885104S-LDPYDVH052-LFJBM60562"; // Chave secreta usada para assinar o token
                                                                             // JWT.
    public static final long EXPIRE_DAYS = 0; // Número de dias até o token JWT expirar.
    public static final long EXPIRE_HOURS = 0; // Número de horas até o token JWT expirar.
    public static final long EXPIRE_MINUTES = 2; // Número de minutos até o token JWT expirar.

    // Construtor privado para evitar a criação de instâncias desta classe.
    private JwtUtils() {

    }

    // Gera uma chave a partir da SECRET_KEY para assinar o token JWT.
    private static Key generateKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    // Calcula a data de expiração do token a partir da data de início.
    private static Date toExpireDate(Date start) {
        LocalDateTime dateTime = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime end = dateTime.plusDays(EXPIRE_DAYS).plusHours(EXPIRE_HOURS).plusMinutes(EXPIRE_MINUTES);
        return Date.from(end.atZone(ZoneId.systemDefault()).toInstant());
    }

    // Cria um token JWT com o nome de usuário e a função do usuário.
    public static JwtToken createToken(String username, String role) {
        Date issuedAt = new Date(); // Data de início do token.
        Date limit = toExpireDate(issuedAt); // Data de expiração do token.
        String token = Jwts.builder() // Inicia a construção do token JWT.
                .setHeaderParam("typ", "JWT") // Define o tipo do token.
                .setSubject(username) // Define o nome de usuário como o assunto do token.
                .setIssuedAt(issuedAt) // Define a data de início do token.
                .setExpiration(limit) // Define a data de expiração do token.
                .signWith(generateKey(), SignatureAlgorithm.HS256) // Assina o token com a chave gerada e o algoritmo
                                                                   // HS256.
                .claim("role", role) // Adiciona a função do usuário como uma reivindicação do token.
                .compact(); // Finaliza a construção do token.
        return new JwtToken(token); // Retorna o token JWT.
    }

    // Extrai as reivindicações (claims) do token JWT.
    private static Claims getClaimFromToken(String token) {
        try {
            return Jwts.parserBuilder() // Inicia a construção do parser do token JWT.
                    .setSigningKey(generateKey()).build() // Define a chave de assinatura do token.
                    .parseClaimsJws(refactorToken(token)).getBody(); // Extrai as reivindicações do token.
        } catch (JwtException ex) {
            log.error(String.format("Token Invalido %s", ex.getMessage())); // Loga a mensagem de erro se o token for
                                                                            // inválido.
        }
        return null; // Retorna null se o token for inválido.
    }

    // Extrai o nome de usuário do token JWT.
    public static String getUsernameFromToken(String token) {
        return getClaimFromToken(token).getSubject(); // Retorna o assunto do token, que é o nome de usuário.
    }

    // Verifica se o token JWT é válido.
    public static boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder() // Inicia a construção do parser do token JWT.
                    .setSigningKey(generateKey()).build() // Define a chave de assinatura do token.
                    .parseClaimsJws(refactorToken(token)); // Tenta parsear o token.
            return true; // Retorna true se o token for válido.
        } catch (JwtException ex) {
            log.error(String.format("Token Invalido %s", ex.getMessage())); // Loga a mensagem de erro se o token for
                                                                            // inválido.
        }
        return false; // Retorna false se o token for inválido.
    }

    // Remove o prefixo "Bearer " do token, se presente.
    private static String refactorToken(String token) {
        if (token.contains(JWT_BEARER)) { // Verifica se o token contém o prefixo "Bearer ".
            return token.substring(JWT_BEARER.length()); // Retorna o token sem o prefixo "Bearer ".
        }
        return token; // Retorna o token refatorado.
    }
}
