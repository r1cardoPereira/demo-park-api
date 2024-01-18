package com.r1cardoPereira.demoparkapi.web.dto;

import lombok.*;


/**
 * Classe UsuarioResponseDto é um Data Transfer Object (DTO) que representa os dados de um usuário para resposta.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UsuarioResponseDto {

    /**
     * ID do usuário.
     */
    private Long id;

    /**
     * Nome de usuário.
     */
    private String username;

    /**
     * Papel do usuário.
     */
    private String role;
    
}