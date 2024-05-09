package com.r1cardoPereira.demoparkapi.web.controller;


import com.r1cardoPereira.demoparkapi.entity.ClienteVaga;
import com.r1cardoPereira.demoparkapi.jwt.JwtUserDetails;
import com.r1cardoPereira.demoparkapi.repository.projection.ClienteVagaProjection;
import com.r1cardoPereira.demoparkapi.service.ClienteService;
import com.r1cardoPereira.demoparkapi.service.ClienteVagaService;
import com.r1cardoPereira.demoparkapi.service.EstacionamentoService;
import com.r1cardoPereira.demoparkapi.service.JasperService;
import com.r1cardoPereira.demoparkapi.web.dto.EstacionamentoCreateDto;
import com.r1cardoPereira.demoparkapi.web.dto.EstacionamentoResponseDto;
import com.r1cardoPereira.demoparkapi.web.dto.PageableDto;
import com.r1cardoPereira.demoparkapi.web.dto.mapper.ClienteVagaMapper;
import com.r1cardoPereira.demoparkapi.web.dto.mapper.PageableMapper;
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
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;

@Tag(name = "Estacionamentos", description = "Operações de registro de entrada e saida de um veiculo do estacionamento.")
@RestController
@RequestMapping("api/v1/estacionamentos")
@RequiredArgsConstructor
public class EstacionamentoController {

    private final EstacionamentoService estacionamentoService;
    private final ClienteVagaService clienteVagaService;
    private final ClienteService clienteService;
    private final JasperService jasperService;

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

    @Operation(
            summary = "Operação de check-out",
            description = "Recurso para dar saída de um veículo do estacionamento. " +
                    "Requisição exige uso de um bearer token. Acesso restrito a Role='ADMIN'",
            security = @SecurityRequirement(name ="Security"),
            parameters = {
                    @Parameter(in = ParameterIn.PATH, name = "recibo",
                            description = "Numero do recibo gerado no check-in")
            },
            responses = {

                    @ApiResponse(responseCode = "200", description = "Recurso atualizado com sucesso",

                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = EstacionamentoResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "403", description = "Recurso não permitido para perfil CLIENTE",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))
                    ),

                    @ApiResponse(responseCode = "404", description = "Número do recibo inexistente ou " +
                                                                     "o veículo já passou pelo check-out.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))
                    )
            })
    @PutMapping("/check-out/{recibo}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EstacionamentoResponseDto> checkOut(@PathVariable String recibo){

        ClienteVaga clienteVaga = estacionamentoService.checkOut(recibo);
        EstacionamentoResponseDto dto = ClienteVagaMapper.toDto(clienteVaga);
        return ResponseEntity.ok(dto);
    }
    @Operation(
            summary = "Localizar os registros de estacionamento do cliente por CPF",
            description = "Localizar os registros de estacionamento do cliente por CPF. Requisição exige perfil ADMIN",
            security = @SecurityRequirement(name ="Security"),
            parameters = {
                    @Parameter(in = ParameterIn.PATH, name = "cpf",
                            description = "Numero do cpf referente ao cliente a ser consultado",
                            required = true),

                    @Parameter(in = ParameterIn.QUERY, name = "page",
                            description = "Representa a pagina retornada",
                            content = @Content(schema = @Schema(type = "integer",defaultValue = "0"))),

                    @Parameter(in = ParameterIn.QUERY, name = "size",
                            description = "Representa total de elementos por pagina",
                            content = @Content(schema = @Schema(type = "integer",defaultValue = "5"))),

                    @Parameter(in = ParameterIn.QUERY, name = "sort",
                            description = "Campo padrão de ordenação 'dataEntrada,asc.'",
                            content = @Content(schema = @Schema(type = "String",defaultValue = "dataEntrada,asc"))),
            },
            responses = {

                    @ApiResponse(responseCode = "200", description = "Recurso localizado com sucesso",

                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = EstacionamentoResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "403", description = "Recurso não permitido para perfil de CLIENTE",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))
                    )
            })
    @GetMapping("/cpf/{cpf}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageableDto> getAllEstacionamentosPorCpf(@PathVariable String cpf,
                                                                   @PageableDefault(size = 5, sort = "dataEntrada",
                                                                   direction = Sort.Direction.ASC) Pageable pageable){
        Page<ClienteVagaProjection> projection = clienteVagaService.buscarTodosPorClienteCpf(cpf, pageable);
        PageableDto dto = PageableMapper.toDto(projection);
        return ResponseEntity.ok(dto);

    }

    @Operation(
            summary = "Localizar os registros de cliente Logado",
            description = "Localizar os registros de cliente Logado. Requisição exige perfil CLIENTE",
            security = @SecurityRequirement(name ="Security"),
            parameters = {

                    @Parameter(in = ParameterIn.QUERY, name = "page",
                            description = "Representa a pagina retornada",
                            content = @Content(schema = @Schema(type = "integer",defaultValue = "0"))),

                    @Parameter(in = ParameterIn.QUERY, name = "size",
                            description = "Representa total de elementos por pagina",
                            content = @Content(schema = @Schema(type = "integer",defaultValue = "5"))),

                    @Parameter(in = ParameterIn.QUERY, name = "sort",
                            description = "Campo padrão de ordenação 'dataEntrada,asc.'",
                            content = @Content(schema = @Schema(type = "String",defaultValue = "dataEntrada,asc"))),
            },
            responses = {

                    @ApiResponse(responseCode = "200", description = "Recurso localizado com sucesso",

                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = EstacionamentoResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "403", description = "Recurso não permitido para perfil de ADMIN",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))
                    )
            })
    @GetMapping
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<PageableDto> getAllEstacionamentosDoCliente(@AuthenticationPrincipal JwtUserDetails user,
                                                                   @PageableDefault(size = 5, sort = "dataEntrada",
                                                                           direction = Sort.Direction.ASC) Pageable pageable){
        Page<ClienteVagaProjection> projection = clienteVagaService.buscarTodosPorUsuarioId(user.getId(), pageable);
        PageableDto dto = PageableMapper.toDto(projection);
        return ResponseEntity.ok(dto);

    }


    @GetMapping("/relatorio")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<Void> getRelatorio(HttpServletResponse response, @AuthenticationPrincipal JwtUserDetails user) throws IOException {
        String cpf = clienteService.buscarPorUsuario(user.getId()).getCpf();
        jasperService.addParams("CPF", cpf);

        byte[] bytes = jasperService.gerarPdf();

        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        response.setHeader("Content-disposition", "inline; filename=" + System.currentTimeMillis() +".pdf");
        response.getOutputStream().write(bytes);


        return  ResponseEntity.ok().build();

    }

}
