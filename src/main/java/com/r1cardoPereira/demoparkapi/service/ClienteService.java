package com.r1cardoPereira.demoparkapi.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.r1cardoPereira.demoparkapi.entity.Cliente;
import com.r1cardoPereira.demoparkapi.exception.CpfUniqueViolationException;
import com.r1cardoPereira.demoparkapi.repository.ClienteRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class ClienteService {

    private final ClienteRepository clienteRepository;


    @Transactional
    public Cliente saveCliente(Cliente cliente) {
        try{
            return clienteRepository.save(cliente);
        } catch(DataIntegrityViolationException ex) {
            throw new CpfUniqueViolationException(
                String.format("CPF '%s' já cadastrado", cliente.getCpf())
            );
        }
    }

}
