package com.r1cardoPereira.demoparkapi.web.dto;

import org.hibernate.validator.constraints.br.CPF;

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

public class ClienteCreateDto {
    
    /**
     * Nome de usuário.
     */
    @NotBlank(message = "Preenchimento obrigatório.")
    @Size(min = 5, max = 100)
    private String nome;

    /**
     * Senha do usuário.
     */
    @NotBlank(message = "Preenchimento obrigatório.")
    @Size(min = 11, max = 11, message = "Digite apenas os nomeros do seu CPF")
    @CPF
    private String cpf;


}
