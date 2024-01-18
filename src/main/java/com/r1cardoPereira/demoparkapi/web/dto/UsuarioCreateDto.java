package com.r1cardoPereira.demoparkapi.web.dto;

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
    private String username;

    /**
     * Senha do usuário.
     */
    private String password;
    
}