package com.r1cardoPereira.demoparkapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.r1cardoPereira.demoparkapi.entity.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

        
    
}
