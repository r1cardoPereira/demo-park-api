package com.r1cardoPereira.demoparkapi.web.dto.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

import com.r1cardoPereira.demoparkapi.entity.Cliente;
import com.r1cardoPereira.demoparkapi.web.dto.ClienteCreateDto;
import com.r1cardoPereira.demoparkapi.web.dto.ClienteResponseDto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)

public class ClienteMapper {

    public static Cliente toCliente(ClienteCreateDto createDto) {
        
        return new ModelMapper().map(createDto, Cliente.class);
    }



    public static ClienteResponseDto toDto(Cliente cliente) {

        return new ModelMapper().map(cliente, ClienteResponseDto.class);
        
    }
    
    // public static List<ClienteResponseDto> toListDto(List<Cliente> clientes){
    //     return clientes.stream().map(cli -> toDto(cli)).collect(Collectors.toList());
    // }
}