package com.r1cardoPereira.demoparkapi.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.r1cardoPereira.demoparkapi.jwt.JwtToken;
import com.r1cardoPereira.demoparkapi.jwt.JwtUserDetailsService;
import com.r1cardoPereira.demoparkapi.web.dto.UsuarioLoginDto;
import com.r1cardoPereira.demoparkapi.web.exception.ErrorMessage;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j // Anotação do Lombok para criar um logger SLF4J.
@RequiredArgsConstructor // Anotação do Lombok para gerar um construtor com parâmetros obrigatórios (final ou @NonNull).
@RestController // Anotação do Spring para indicar que essa classe é um controlador REST.
@RequestMapping("/api/v1") // Anotação do Spring para mapear requisições web para esse controlador usando o caminho especificado.
public class AuthenticacaoController { // Declaração da classe AuthenticacaoController.

    private final JwtUserDetailsService detailsService; // Injeção de dependência para o serviço de detalhes do usuário JWT.
    private final AuthenticationManager authenticationManager; // Injeção de dependência para o gerenciador de autenticação.

    @PostMapping("/auth") // Anotação do Spring para mapear requisições POST para o método autenticar().
    public ResponseEntity<?> autenticar(@RequestBody @Valid UsuarioLoginDto dto, HttpServletRequest request ){ // Método para autenticar um usuário.

        log.info("Processo de autenticação pelo login {}", dto.getUsername()); // Log de informação sobre o processo de autenticação.
        try{
            UsernamePasswordAuthenticationToken authenticationToken = // Criação de um token de autenticação com nome de usuário e senha.
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());
                authenticationManager.authenticate(authenticationToken); // Autenticação do token.
                JwtToken token = detailsService.getTokenAuthenticated(dto.getUsername()); // Obtenção do token JWT para o usuário autenticado.
                return ResponseEntity.ok(token); // Retorno do token JWT em caso de sucesso na autenticação.

        }catch(AuthenticationException ex){ // Captura de exceções de autenticação.
            log.warn("Bad credentials from username ", dto.getUsername()); // Log de aviso sobre credenciais inválidas.
        }

        return ResponseEntity // Retorno de uma resposta de erro em caso de falha na autenticação.
                .badRequest()
                .body(new ErrorMessage(request, HttpStatus.BAD_REQUEST,"Credenciais inválidas."));

    }
    
} 
