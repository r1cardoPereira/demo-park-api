package com.r1cardoPereira.demoparkapi.web.dto.mapper;

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

}