package com.r1cardoPereira.demoparkapi.web.dto.mapper;

import com.r1cardoPereira.demoparkapi.entity.Vaga;
import com.r1cardoPereira.demoparkapi.web.dto.VagaCreateDto;
import com.r1cardoPereira.demoparkapi.web.dto.VagaResponseDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;



@NoArgsConstructor(access = AccessLevel.PRIVATE)

public class VagaMapper {

    public static Vaga toVaga(VagaCreateDto createDto){
        return new ModelMapper().map(createDto, Vaga.class);

    }

    public static VagaResponseDto toDto(Vaga vaga){

        return  new ModelMapper().map(vaga, VagaResponseDto.class);
    }
}
