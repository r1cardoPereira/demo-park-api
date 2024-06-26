package com.r1cardoPereira.demoparkapi.service;

import com.r1cardoPereira.demoparkapi.entity.ClienteVaga;
import com.r1cardoPereira.demoparkapi.exception.EntityNotFoundException;
import com.r1cardoPereira.demoparkapi.repository.ClienteVagaRepository;
import com.r1cardoPereira.demoparkapi.repository.projection.ClienteVagaProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClienteVagaService {

    private final ClienteVagaRepository clienteVagaRepository;

    @Transactional
    public ClienteVaga salvar( ClienteVaga clienteVaga){
        return clienteVagaRepository.save(clienteVaga);
    }

    @Transactional(readOnly = true)
    public ClienteVaga buscarPorRecibo(String recibo) {
        return clienteVagaRepository.findByReciboAndDataSaidaIsNull(recibo).orElseThrow(
                () -> new EntityNotFoundException(
                        String.format("Recibo '%s' não encontrado no sistema ou cliente já realizou check-out", recibo)
                )
        );
    }
    @Transactional(readOnly = true)
    public long getTotalDeVezesEstacionamentoCompleto(String cpf) {

        return clienteVagaRepository.countByClienteCpfAndDataSaidaIsNotNull(cpf);
    }
    @Transactional(readOnly = true)
    public Page<ClienteVagaProjection> buscarTodosPorClienteCpf(String cpf, Pageable pageable) {

        return  clienteVagaRepository.findAllByClienteCpf(cpf,pageable);
    }
    @Transactional(readOnly = true)
    public Page<ClienteVagaProjection> buscarTodosPorUsuarioId(long id, Pageable pageable) {
        return clienteVagaRepository.findAllByClienteUsuarioId(id,pageable);
    }
}
