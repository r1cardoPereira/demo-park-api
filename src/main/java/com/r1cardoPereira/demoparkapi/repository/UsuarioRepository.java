package com.r1cardoPereira.demoparkapi.repository;

import java.util.Optional;

// Importando as bibliotecas necessárias
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.r1cardoPereira.demoparkapi.entity.Usuario;

/**
 * Interface UsuarioRepository é um repositório JPA para a entidade Usuario.
 * <p>
 * Esta interface estende JpaRepository, que é uma interface do Spring Data JPA
 * que contém a funcionalidade
 * básica de CRUD (Criar, Ler, Atualizar, Deletar). Ao estender JpaRepository,
 * esta interface herda todos
 * os métodos padrão de CRUD para a entidade Usuario.
 * <p>
 * O Spring Data JPA criará uma implementação desta interface automaticamente em
 * tempo de execução.
 *
 * @see org.springframework.data.jpa.repository.JpaRepository
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsername(String username);

    @Query("select u.role from Usuario u where u.username like :username")
    Usuario.Role findRoleByUsername(String username);

}
