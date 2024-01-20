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

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/usuarios")

public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<UsuarioResponseDto> create(@Valid @RequestBody UsuarioCreateDto usuarioCreateDto) {

        Usuario user = usuarioService.save(UsuarioMapper.toUsuario(usuarioCreateDto));

        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioMapper.toDto(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> getUserById(@PathVariable Long id) {

        Usuario user = usuarioService.getByid(id);

        return ResponseEntity.status(HttpStatus.OK).body(UsuarioMapper.toDto(user));

    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDto>> getAll() {

        List<Usuario> users = usuarioService.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(UsuarioMapper.toListDto(users));

    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updatePasswordById (@PathVariable Long id, @Valid @RequestBody UsuarioSenhaDto dto) {
        Usuario user = usuarioService.updatePassword(id,dto.getSenhaAtual(),dto.getNovaSenha(),dto.getConfirmaSenha());

        return ResponseEntity.noContent().build();

    }

}
