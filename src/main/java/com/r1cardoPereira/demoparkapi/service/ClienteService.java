package com.r1cardoPereira.demoparkapi.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.r1cardoPereira.demoparkapi.entity.Cliente;
import com.r1cardoPereira.demoparkapi.entity.Usuario.Role;
import com.r1cardoPereira.demoparkapi.exception.CpfUniqueViolationException;
import com.r1cardoPereira.demoparkapi.exception.EntityNotFoundException;
import com.r1cardoPereira.demoparkapi.repository.ClienteRepository;
import com.r1cardoPereira.demoparkapi.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;


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

}
