package com.r1cardoPereira.demoparkapi.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.r1cardoPereira.demoparkapi.entity.Cliente;
import com.r1cardoPereira.demoparkapi.repository.projection.ClienteProjection;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    @Query("select c from Cliente c")
    Page<ClienteProjection>findAllPageable(Pageable pageable);

}
