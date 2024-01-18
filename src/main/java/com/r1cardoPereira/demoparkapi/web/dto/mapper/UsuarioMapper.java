package com.r1cardoPereira.demoparkapi.web.dto.mapper;


import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import com.r1cardoPereira.demoparkapi.entity.Usuario;
import com.r1cardoPereira.demoparkapi.web.dto.UsuarioCreateDto;
import com.r1cardoPereira.demoparkapi.web.dto.UsuarioResponseDto;

/**
 * Classe UsuarioMapper é responsável por mapear os objetos de Usuario e seus DTOs.
 */
public class UsuarioMapper {

    /**
     * Método estático que converte um UsuarioCreateDto em um objeto Usuario.
     * @param createDto - DTO que contém os dados para a criação de um novo usuário.
     * @return Usuario - Retorna um objeto Usuario mapeado a partir do DTO.
     */
    public static Usuario toUsuario(UsuarioCreateDto createDto){
        return new ModelMapper().map(createDto, Usuario.class);
    }

    /**
     * Método estático que converte um objeto Usuario em um UsuarioResponseDto.
     * @param usuario - Objeto Usuario que será convertido em DTO.
     * @return UsuarioResponseDto - Retorna um DTO mapeado a partir do objeto Usuario.
     */
    public static UsuarioResponseDto toDto(Usuario usuario ){
        // Extrai o nome do papel do usuário, removendo o prefixo "ROLE_"
        String role = usuario.getRole().name().substring("ROLE_".length());

        // Define um mapeamento personalizado para o ModelMapper
        PropertyMap<Usuario, UsuarioResponseDto> props = new PropertyMap<Usuario ,UsuarioResponseDto>(){
            @Override
            protected void configure(){
                // Configura o mapeamento do papel do usuário
                map().setRole(role);
            }
        };

        // Cria um novo ModelMapper e adiciona o mapeamento personalizado
        ModelMapper mapper =  new ModelMapper();
        mapper.addMappings(props);

        // Retorna o DTO mapeado a partir do objeto Usuario
        return mapper.map(usuario, UsuarioResponseDto.class);
    }

    /**
     * Converte uma lista de entidades Usuario para uma lista de DTOs UsuarioResponseDto.
     *
     * @param usuarios - A lista de entidades Usuario a ser convertida.
     * @return Uma lista de DTOs UsuarioResponseDto correspondente à lista de entidades Usuario fornecida.
     */
    public static List<UsuarioResponseDto> toListDto(List<Usuario> usuarios){
        return usuarios.stream().map(user -> toDto(user)).collect(Collectors.toList());
    }
}