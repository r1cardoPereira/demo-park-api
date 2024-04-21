package com.r1cardoPereira.demoparkapi.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.r1cardoPereira.demoparkapi.exception.CpfUniqueViolationException;
import com.r1cardoPereira.demoparkapi.exception.EntityNotFoundException;
import com.r1cardoPereira.demoparkapi.exception.PasswordInvalidException;
import com.r1cardoPereira.demoparkapi.exception.UsernameUniqueViolationException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * Classe ApiExceptionHandler.
 * Esta classe é um manipulador de exceções global para a API.
 * Ela usa a anotação @RestControllerAdvice para indicar que é um componente que
 * deve ser usado para tratar exceções em todos os controladores REST.
 * 
 * @author Ricardo Pereira
 * @version 1.0
 */
@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {


    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorMessage> accessDeniedException(AccessDeniedException ex,
        HttpServletRequest request) {

        log.error("API error -", ex);
        return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .contentType(MediaType.APPLICATION_JSON)
            .body(new ErrorMessage(request, HttpStatus.FORBIDDEN, ex.getMessage()));
    }

    /**
     * Este método é um manipulador de exceções específico para
     * MethodArgumentNotValidException.
     * Esta exceção é lançada quando a validação falha para um argumento de método
     * anotado com @Valid.
     * 
     * @param ex      A exceção lançada
     * @param request A requisição HTTP
     * @param result  O resultado da validação
     * @return Uma resposta HTTP com o status 422 (Unprocessable Entity), indicando
     *         que a entidade fornecida com a requisição é sintaticamente correta,
     *         mas semanticamente errônea.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> methodArgumentNotValidException(MethodArgumentNotValidException ex,
            HttpServletRequest request,
            BindingResult result) {

        log.error("API Error - ", ex);
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, HttpStatus.UNPROCESSABLE_ENTITY, "Campo(s) Invalido(s)", result));

    }

    /**
     * Este método é um manipulador de exceções específico para UsernameUniqueViolationException.
     * Esta exceção é lançada quando há uma violação de unicidade do nome de usuário.
     * 
     * @param ex A exceção lançada
     * @param request A requisição HTTP
     * @return Uma resposta HTTP com o status 409 (Conflict), indicando que a requisição não pôde ser concluída devido a um conflito com o estado atual do recurso.
     */
    @ExceptionHandler({UsernameUniqueViolationException.class, CpfUniqueViolationException.class})
    public ResponseEntity<ErrorMessage> usernameUniqueViolationException(RuntimeException ex,
        HttpServletRequest request) {

        log.error("API error - ", ex);
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .contentType(MediaType.APPLICATION_JSON)
            .body(new ErrorMessage(request, HttpStatus.CONFLICT, ex.getMessage()));
    }

    /**
     * Este método é um manipulador de exceções específico para EntityNotFoundException.
     * Esta exceção é lançada quando uma entidade não é encontrada.
     * 
     * @param ex A exceção lançada
     * @param request A requisição HTTP
     * @return Uma resposta HTTP com o status 404 (Not Found), indicando que o recurso solicitado não pôde ser encontrado no servidor.
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorMessage> entityNotFoundException(RuntimeException ex,
        HttpServletRequest request) {

        log.error("API error -", ex);
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .contentType(MediaType.APPLICATION_JSON)
            .body(new ErrorMessage(request, HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    /**
     * Este método é um manipulador de exceções específico para PasswordInvalidException.
     * Esta exceção é lançada quando uma senha inválida é fornecida.
     * 
     * @param ex A exceção lançada
     * @param request A requisição HTTP
     * @return Uma resposta HTTP com o status 400 (Bad Request), indicando que o servidor não pôde entender a requisição devido à sintaxe inválida.
     */
    @ExceptionHandler(PasswordInvalidException.class)
    public ResponseEntity<ErrorMessage> passwordInvalidException(RuntimeException ex,
        HttpServletRequest request) {

        log.error("API error -", ex);
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .contentType(MediaType.APPLICATION_JSON)
            .body(new ErrorMessage(request, HttpStatus.BAD_REQUEST, ex.getMessage()));
    }
}
