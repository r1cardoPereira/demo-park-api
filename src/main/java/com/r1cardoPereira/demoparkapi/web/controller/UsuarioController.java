package com.r1cardoPereira.demoparkapi.web.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Classe UsuarioController.
 * Esta classe é responsável por manipular as requisições HTTP relacionadas aos usuários.
 * Ela usa a anotação @RestController para indicar que é um controlador REST.
 * Ela usa a anotação @RequestMapping para mapear as requisições para "api/v1/usuarios".
 * Ela usa a anotação @RequiredArgsConstructor para gerar automaticamente um construtor com um parâmetro para cada campo final na classe.
 */
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
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> getUserById(@PathVariable Long id) {

        Usuario user = usuarioService.getByid(id);

        return ResponseEntity.status(HttpStatus.OK).body(UsuarioMapper.toDto(user));

    }

    /**
     * Este método manipula as requisições GET para buscar todos os usuários.
     * 
     * @return Uma resposta com o status 200 (OK) e a lista de usuários.
     */
    @GetMapping
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
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updatePasswordById (@PathVariable Long id, @Valid @RequestBody UsuarioSenhaDto dto) {
        Usuario user = usuarioService.updatePassword(id,dto.getSenhaAtual(),dto.getNovaSenha(),dto.getConfirmaSenha());

        return ResponseEntity.noContent().build();

    }

}