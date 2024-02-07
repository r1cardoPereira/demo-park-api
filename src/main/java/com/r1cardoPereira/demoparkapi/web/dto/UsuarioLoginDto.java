package com.r1cardoPereira.demoparkapi.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UsuarioLoginDto {

    
    @NotBlank(message = "Preenchimento obrigatório.")
    @Email(message = "Formato de e-mail invalido.", regexp = "^[a-z0-9.+-]+@[a-z0-9.-]+\\.[a-z]{2,}$")
    private String username;
    
    @NotBlank(message = "Preenchimento obrigatório.")
    @Size(min = 6, max = 6, message = "A senha precisa conter 6 caracteres.")
    private String password;

    
}
