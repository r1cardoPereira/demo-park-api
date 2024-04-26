package com.r1cardoPereira.demoparkapi.web.controller;


import com.r1cardoPereira.demoparkapi.entity.Vaga;
import com.r1cardoPereira.demoparkapi.service.VagaService;
import com.r1cardoPereira.demoparkapi.web.dto.UsuarioResponseDto;
import com.r1cardoPereira.demoparkapi.web.dto.VagaCreateDto;
import com.r1cardoPereira.demoparkapi.web.dto.VagaResponseDto;
import com.r1cardoPereira.demoparkapi.web.dto.mapper.VagaMapper;
import com.r1cardoPereira.demoparkapi.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Tag(name = "Vagas", description = "Contém todas as operações relativas ao recurso de uma vaga.")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/vagas")
public class VagaController {

    private final VagaService vagaService;

    @Operation(
        summary = "Criar uma nova Vaga",
        description = "Recurso para criar nova vaga, exige autenticação de ADMIN",
        responses = {

            @ApiResponse(responseCode = "201", description = "Recurso criado com sucesso",
                    headers = @Header(name = HttpHeaders.LOCATION,description = "URL do recurso criado")),


            @ApiResponse(responseCode = "409", description = "Vaga já cadastrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class))),


            @ApiResponse(responseCode = "422", description = "Recurso não processado por dados de entrada invalidos",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)))
            })


    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> create(@Valid @RequestBody VagaCreateDto vagaCreateDto){

        Vaga vaga = VagaMapper.toVaga(vagaCreateDto);
        vagaService.saveVaga(vaga);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri().path("/{codigo}")
                .buildAndExpand(vaga.getCodigo())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @Operation(
        summary = "Recuperar uma vaga",
        description = "Recuperar uma Vaga apartir do codigo, recurso disponivel somente para ADMIN",
        responses = {

            @ApiResponse(responseCode = "200", description = "Recurso encontrado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VagaResponseDto.class))),

            @ApiResponse(responseCode = "404", description = "Vaga não localizada.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)))
            })


    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VagaResponseDto> getByCodigo(@PathVariable String codigo){

        Vaga vaga = vagaService.buscarPorCodigo(codigo);

        return  ResponseEntity.status(HttpStatus.OK).body(VagaMapper.toDto(vaga));
    }

}
