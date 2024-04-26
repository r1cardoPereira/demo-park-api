package com.r1cardoPereira.demoparkapi.repository;

import com.r1cardoPereira.demoparkapi.entity.Vaga;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VagaRepository extends JpaRepository <Vaga, Long>{


   Optional<Vaga> findByCodigo(String codigo);
}
