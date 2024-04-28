package com.r1cardoPereira.demoparkapi.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class EstacionamentoCreateDto {

    @NotBlank
    @Size(min = 8,max = 8)
    @Pattern(regexp = "[A-Z]{3}-[0-9]{4}", message = "A Plaga deve seguir o padrão XXX-000")
    private String placa;
    @NotBlank
    private String marca;
    @NotBlank
    private String modelo;
    @NotBlank
    private String cor;
    @NotBlank
    @Size(min = 11, max = 11)
    @CPF
    private String clienteCpf;





}
