package com.r1cardoPereira.demoparkapi.web.controller;


import com.r1cardoPereira.demoparkapi.entity.ClienteVaga;
import com.r1cardoPereira.demoparkapi.service.ClienteVagaService;
import com.r1cardoPereira.demoparkapi.service.EstacionamentoService;
import com.r1cardoPereira.demoparkapi.web.dto.EstacionamentoCreateDto;
import com.r1cardoPereira.demoparkapi.web.dto.EstacionamentoResponseDto;
import com.r1cardoPereira.demoparkapi.web.dto.mapper.ClienteVagaMapper;
import com.r1cardoPereira.demoparkapi.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Tag(name = "Estacionamentos", description = "Operações de registro de entrada e saida de um veiculo do estacionamento.")
@RestController
@RequestMapping("api/v1/estacionamentos")
@RequiredArgsConstructor
public class EstacionamentoController {

    private final EstacionamentoService estacionamentoService;
    private final ClienteVagaService clienteVagaService;

    @Operation(
            summary = "Oparação de check-in",
            description = "Recurso para dar entrada de veiculo no estacionamento." +
                    "Requisição exige uso de autenticação de nivel 'ADMIN'",
            security = @SecurityRequirement(name ="Security"),
            responses = {

                    @ApiResponse(responseCode = "201", description = "Recurso criado com sucesso",
                            headers = @Header(name = HttpHeaders.LOCATION,description = "URL do recurso criado"),
                            content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EstacionamentoResponseDto.class))
                    ),

                    @ApiResponse(responseCode = "403", description = "Recurso disponivel somente para usuario com perfil 'ADMIN'",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))
                    ),

                    @ApiResponse(responseCode = "404", description = "Causas possíveis: <br/>"+
                            "- CPF do cliente não cadastrado no sistema; <br/>"+
                            "- Nenhuma vaga livre foi localizada;",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))
                    ),


                    @ApiResponse(responseCode = "422", description = "Recurso não processado por dados de entrada invalidos",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)))
            })

    @PostMapping("/check-in")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EstacionamentoResponseDto> checkIn(@RequestBody @Valid EstacionamentoCreateDto dto){

        ClienteVaga clienteVaga = ClienteVagaMapper.toClienteVaga(dto);
        estacionamentoService.checkIn(clienteVaga);
        EstacionamentoResponseDto responseDto = ClienteVagaMapper.toDto(clienteVaga);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri().path("/{recibo}")
                .buildAndExpand(clienteVaga.getRecibo())
                .toUri();
        return ResponseEntity.created(location).body(responseDto);




    }

    @Operation(
            summary = "Localizar um veiculo no estacionamento",
            description = "Recurso para retornar um no estacionamento." +
                    "pelo n recibo. Requisição exige uso de um Bearer Token  ",
            security = @SecurityRequirement(name ="Security"),
            parameters = {
                    @Parameter(in = ParameterIn.PATH, name = "recibo",
                    description = "Numero do recibo gerado no check-in")
            },
            responses = {

                    @ApiResponse(responseCode = "200", description = "Recurso localizado com sucesso",

                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = EstacionamentoResponseDto.class))
                    ),

                    @ApiResponse(responseCode = "404", description = "Numero de recibo não encontrado",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))
                    )
            })

    @GetMapping("/check-in/{recibo}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")
    public ResponseEntity<EstacionamentoResponseDto> getByRecibo(@PathVariable String recibo){

        ClienteVaga clienteVaga = clienteVagaService.buscarPorRecibo(recibo);
        EstacionamentoResponseDto dto = ClienteVagaMapper.toDto(clienteVaga);
        return ResponseEntity.ok(dto);

    }

}
