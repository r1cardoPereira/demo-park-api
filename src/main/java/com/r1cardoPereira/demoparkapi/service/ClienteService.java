package com.r1cardoPereira.demoparkapi.service;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.r1cardoPereira.demoparkapi.entity.Cliente;
import com.r1cardoPereira.demoparkapi.exception.CpfUniqueViolationException;
import com.r1cardoPereira.demoparkapi.exception.EntityNotFoundException;
import com.r1cardoPereira.demoparkapi.repository.ClienteRepository;
import com.r1cardoPereira.demoparkapi.repository.projection.ClienteProjection;

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

    
    @Transactional(readOnly = true)
    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id).orElseThrow(
            () -> new EntityNotFoundException(String.format("Cliente id=%s não encontrado.", id))
        );
    }

    @Transactional(readOnly = true)
    public Page<ClienteProjection> buscarTodos(Pageable pageable){
        
        return clienteRepository.findAllPageable(pageable);
    }

}
