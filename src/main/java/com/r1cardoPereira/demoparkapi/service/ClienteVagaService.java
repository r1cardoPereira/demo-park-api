package com.r1cardoPereira.demoparkapi.service;

import com.r1cardoPereira.demoparkapi.entity.ClienteVaga;
import com.r1cardoPereira.demoparkapi.repository.ClienteVagaRepository;
import lombok.RequiredArgsConstructor;
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
}
