package com.r1cardoPereira.demoparkapi.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;


/**
 * Classe UsuarioCreateDto é um Data Transfer Object (DTO) que representa os dados de um usuário para criação.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UsuarioCreateDto {

    /**
     * Nome de usuário.
     */
    @NotBlank(message = "Preenchimento obrigatório.")
    @Email(message = "Formato de e-mail invalido.", regexp = "^[a-z0-9.+-]+@[a-z0-9.-]+\\.[a-z]{2,}$")
    private String username;

    /**
     * Senha do usuário.
     */
    @NotBlank(message = "Preenchimento obrigatório.")
    @Size(min = 6, max = 6, message = "A senha precisa conter 6 caracteres.")
    private String password;
    
}