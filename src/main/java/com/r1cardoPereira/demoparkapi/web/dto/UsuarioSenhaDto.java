package com.r1cardoPereira.demoparkapi.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UsuarioSenhaDto {

    
    @NotBlank(message = "Preenchimento obrigatório.")
    @Size(min = 6, max = 6, message = "A senha precisa conter 6 caracteres.")
    private String senhaAtual;
    
    @NotBlank(message = "Preenchimento obrigatório.")
    @Size(min = 6, max = 6, message = "A senha precisa conter 6 caracteres.")
    private String novaSenha;
    
    @NotBlank(message = "Preenchimento obrigatório.")
    @Size(min = 6, max = 6, message = "A senha precisa conter 6 caracteres.")
    private String confirmaSenha;
    
}
