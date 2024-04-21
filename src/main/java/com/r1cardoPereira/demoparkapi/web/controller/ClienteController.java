package com.r1cardoPereira.demoparkapi.web.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.r1cardoPereira.demoparkapi.entity.Cliente;
import com.r1cardoPereira.demoparkapi.jwt.JwtUserDetails;
import com.r1cardoPereira.demoparkapi.service.ClienteService;
import com.r1cardoPereira.demoparkapi.service.UsuarioService;
import com.r1cardoPereira.demoparkapi.web.dto.ClienteResponseDto;
import com.r1cardoPereira.demoparkapi.web.dto.mapper.ClienteCreateDto;
import com.r1cardoPereira.demoparkapi.web.dto.mapper.ClienteMapper;
import com.r1cardoPereira.demoparkapi.web.exception.ErrorMessage;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Clientes", description = "Contém todas as operações relativas aos recursos para cadastro, edição e leitura de um cliente")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/clientes")
public class ClienteController {

    private final ClienteService clienteService;
    private final UsuarioService usuarioService;

    @Operation(
        summary = "Criar um novo Cliente",
        description = "Recurso para criar um novo cliente",
        responses = {
            
            @ApiResponse(responseCode = "201", description = "Recurso criado com sucesso",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ClienteResponseDto.class))),
            
            
            @ApiResponse(responseCode = "409", description = "Cliente CPF já cadastrado no sistema",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ErrorMessage.class))),
            
            
            @ApiResponse(responseCode = "422", description = "Recurso não processado por falta de dados ou dados invalidos",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ErrorMessage.class))),
            
            @ApiResponse(responseCode = "403", description = "Recurso não permitido ao Perfil ADMIN",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ErrorMessage.class)))
            
            })

    
    @PostMapping
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<ClienteResponseDto> create(@Valid @RequestBody ClienteCreateDto clienteCreateDto,
                                                    @AuthenticationPrincipal JwtUserDetails userDetails) {
        
        Cliente cliente = ClienteMapper.toCliente(clienteCreateDto);
        cliente.setUsuario(usuarioService.getByid(userDetails.getId()));
        clienteService.saveCliente(cliente);

        return ResponseEntity.status(HttpStatus.CREATED).body(ClienteMapper.tDto(cliente));
    }

    
}
