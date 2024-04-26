package com.r1cardoPereira.demoparkapi.service;

import com.r1cardoPereira.demoparkapi.entity.Vaga;
import com.r1cardoPereira.demoparkapi.exception.CodigoUniqueViolationException;
import com.r1cardoPereira.demoparkapi.exception.EntityNotFoundException;
import com.r1cardoPereira.demoparkapi.repository.VagaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor

public class VagaService {

    private final VagaRepository vagaRepository;

    @Transactional
    public Vaga saveVaga(Vaga vaga) {

        try {
            return vagaRepository.save(vaga);

        } catch (DataIntegrityViolationException ex) {
            throw new CodigoUniqueViolationException(
                    String.format("Vaga com codigo '%s'Já cadastrada", vaga.getCodigo())
            );
        }
    }


    @Transactional(readOnly = true)
    public Vaga buscarPorCodigo(String codigo){

        return vagaRepository.findByCodigo(codigo).orElseThrow(
                () -> new EntityNotFoundException(
                        String.format("Codigo %s não encontrado.", codigo)));
    }
}
