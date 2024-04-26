package com.r1cardoPereira.demoparkapi.web.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class VagaResponseDto {

    private Long id;

    private String codigo;

    private String status;
}
