package com.r1cardoPereira.demoparkapi.web.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.ToString;

/**
 * Classe ErrorMessage.
 * Esta classe é responsável por modelar uma mensagem de erro personalizada.
 * 
 * @author Ricardo Pereira
 * @version 1.0
 */
@Getter
@ToString
public class ErrorMessage {

    // Variável que armazena o caminho da requisição
    private String path;

    // Variável que armazena o método da requisição
    private String method;

    // Variável que armazena o status HTTP da resposta
    private Integer status;

    // Variável que armazena o texto do status HTTP
    private String statusText;

    // Variável que armazena a mensagem de erro
    private String message;

    
    /**
     * A anotação @JsonInclude é usada para controlar a serialização de propriedades com valores nulos ou padrão.
     * JsonInclude.Include.NON_NULL indica que apenas propriedades com valores não nulos serão incluídas na serialização.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    // Mapa que armazena os erros de campo
    private Map<String,String> errors;

    /**
     * Construtor padrão.
     */
    public ErrorMessage() {
    }

    /**
     * Construtor com parâmetros.
     * 
     * @param request A requisição HTTP
     * @param status O status HTTP
     * @param message A mensagem de erro
     */
    public ErrorMessage(HttpServletRequest request, HttpStatus status, String message) {
        this.path = request.getRequestURI();
        this.method = request.getMethod();
        this.status = status.value();
        this.statusText = status.getReasonPhrase();
        this.message = message;
    }

    /**
     * Construtor com parâmetros.
     * 
     * @param request A requisição HTTP
     * @param status O status HTTP
     * @param message A mensagem de erro
     * @param result O resultado da validação
     */
    public ErrorMessage(HttpServletRequest request, HttpStatus status, String message, BindingResult result){
        this.path = request.getRequestURI();
        this.method = request.getMethod();
        this.status = status.value();
        this.statusText = status.getReasonPhrase();
        this.message = message;
        addErrors(result);
    }

    /**
     * Método responsável por adicionar erros de campo ao mapa de erros.
     * 
     * @param result O resultado da validação
     */
    private void addErrors(BindingResult result) {
        this.errors = new HashMap<>();
        for(FieldError fieldError : result.getFieldErrors()){
            this.errors.put(fieldError.getField(),fieldError.getDefaultMessage());
        }
    }
}
