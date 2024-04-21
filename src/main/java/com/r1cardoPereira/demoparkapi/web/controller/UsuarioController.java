package com.r1cardoPereira.demoparkapi.web.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.r1cardoPereira.demoparkapi.entity.Usuario;
import com.r1cardoPereira.demoparkapi.service.UsuarioService;
import com.r1cardoPereira.demoparkapi.web.dto.UsuarioCreateDto;
import com.r1cardoPereira.demoparkapi.web.dto.UsuarioResponseDto;
import com.r1cardoPereira.demoparkapi.web.dto.UsuarioSenhaDto;
import com.r1cardoPereira.demoparkapi.web.dto.mapper.UsuarioMapper;
import com.r1cardoPereira.demoparkapi.web.exception.ErrorMessage;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Classe UsuarioController.
 * Esta classe é responsável por manipular as requisições HTTP relacionadas aos usuários.
 * Ela usa a anotação @RestController para indicar que é um controlador REST.
 * Ela usa a anotação @RequestMapping para mapear as requisições para "api/v1/usuarios".
 * Ela usa a anotação @RequiredArgsConstructor para gerar automaticamente um construtor com um parâmetro para cada campo final na classe.
 */

@Tag(name = "Usuarios", description = "Contém todas as operações relativas aos recursos para cadastro, edição e leitura de um usuário")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/usuarios")
public class UsuarioController {

    /**
     * O serviço de usuários, usado para realizar operações relacionadas aos usuários.
     */
    private final UsuarioService usuarioService;

    
    
    
    
    
    
    /**
     * Este método manipula as requisições POST para criar um novo usuário.
     * 
     * @param usuarioCreateDto O DTO com os dados do usuário a ser criado.
     * @return Uma resposta com o status 201 (Created) e o usuário criado.
     */
    
    
    @Operation(
        summary = "Criar um novo Usuário",
        description = "Recurso para criar um novo usuário",
        responses = {
            
            @ApiResponse(responseCode = "201", description = "Recurso criado com sucesso",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = UsuarioResponseDto.class))),
            
            
            @ApiResponse(responseCode = "409", description = "Usuário e-mail já cadastrado no sistema",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ErrorMessage.class))),
            
            
            @ApiResponse(responseCode = "422", description = "Recurso não processado por dados de entrada invalidos",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ErrorMessage.class)))
            })


    @PostMapping
    public ResponseEntity<UsuarioResponseDto> create(@Valid @RequestBody UsuarioCreateDto usuarioCreateDto) {

        Usuario user = usuarioService.save(UsuarioMapper.toUsuario(usuarioCreateDto));

        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioMapper.toDto(user));
    }

    
    
    
    
    /**
     * Este método manipula as requisições GET para buscar um usuário pelo ID.
     * 
     * @param id O ID do usuário a ser buscado.
     * @return Uma resposta com o status 200 (OK) e o usuário encontrado.
     */

    @Operation(
        summary = "Recuperar Usuario pelo ID",
        security = @SecurityRequirement(name ="Security"),
        description = "Requisição exige Bearer Token. Acesso Restrito a ADMIN|CLIENTE",
        responses = {
            
            @ApiResponse(responseCode = "200", description = "Recurso encontrado com sucesso",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = UsuarioResponseDto.class))),
                
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão para acessar este recurso",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ErrorMessage.class))),
            
            @ApiResponse(responseCode = "404", description = "Recurso não encontrado.",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ErrorMessage.class)))
            })

    @GetMapping("/{id}")
    @PreAuthorize(value = "hasRole('ADMIN') OR (hasRole('CLIENTE') AND #id == authentication.principal.id)")
    public ResponseEntity<UsuarioResponseDto> getUserById(@PathVariable Long id) {

        Usuario user = usuarioService.getByid(id);

        return ResponseEntity.status(HttpStatus.OK).body(UsuarioMapper.toDto(user));

    }

    
    
    
    
    /**
     * Este método manipula as requisições GET para buscar todos os usuários.
     * 
     * @return Uma resposta com o status 200 (OK) e a lista de usuários.
     */
    
    @Operation(
        summary = "Listar todos os Usuários Cadastrados",
        security = @SecurityRequirement(name ="Security"),
        description = "Requisição exige Bearer Token. Acesso Restrito a ADMIN.",
        responses = {
            
        @ApiResponse(responseCode = "200", description = "Usuários listados com sucesso.",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = UsuarioCreateDto.class))),
        
        @ApiResponse(responseCode = "403", description = "Usuário sem permissão para acessar este recurso",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ErrorMessage.class))),
            })
    
    
    @GetMapping
    @PreAuthorize(value = "hasRole('ADMIN')")
    public ResponseEntity<List<UsuarioResponseDto>> getAll() {

        List<Usuario> users = usuarioService.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(UsuarioMapper.toListDto(users));

    }

    
    
    
    
    /**
     * Este método manipula as requisições PATCH para atualizar a senha de um usuário pelo ID.
     * 
     * @param id O ID do usuário a ter a senha atualizada.
     * @param dto O DTO com a senha atual, a nova senha e a confirmação da nova senha.
     * @return Uma resposta com o status 204 (No Content).
     */

    @Operation(
        summary = "Altera senha do Usuario",
        security = @SecurityRequirement(name ="Security"),
        description = "Requisição exige Bearer Token. Acesso Restrito a ADMIN|CLIENTE.",
        responses = {
            
            @ApiResponse(responseCode = "204", description = "Senha Alterada com Sucesso"),
            
            @ApiResponse(responseCode = "400", description = "Senha não confere...",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ErrorMessage.class))),
                
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão para acessar este recurso",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ErrorMessage.class))),
            
            @ApiResponse(responseCode = "422", description = "Campos invalidos ou mal formatados.",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PatchMapping("/{id}")
    @PreAuthorize(value = "hasAnyRole('ADMIN','CLIENTE') AND (#id == authentication.principal.id)")
    public ResponseEntity<Void> updatePasswordById (@PathVariable Long id, @Valid @RequestBody UsuarioSenhaDto dto) {
        usuarioService.updatePassword(id,dto.getSenhaAtual(),dto.getNovaSenha(),dto.getConfirmaSenha());

        return ResponseEntity.noContent().build();

    }

}