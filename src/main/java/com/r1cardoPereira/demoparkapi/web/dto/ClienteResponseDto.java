package com.r1cardoPereira.demoparkapi.web.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class ClienteResponseDto {

    private Long id;

    private String nome;

    private String cpf;

}
