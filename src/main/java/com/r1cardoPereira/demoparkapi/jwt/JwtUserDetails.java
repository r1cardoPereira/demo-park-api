package com.r1cardoPereira.demoparkapi.jwt;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

import com.r1cardoPereira.demoparkapi.entity.Usuario;

// A classe JwtUserDetails estende a classe User do Spring Security.
// Ela é usada para representar um usuário autenticado no sistema.
public class JwtUserDetails extends User {

    // Atributo que representa o usuário.
    private Usuario usuario;

    // Construtor da classe. Recebe um objeto do tipo Usuario.
    public JwtUserDetails(Usuario usuario) {
        // Chama o construtor da classe pai (User) passando o nome de usuário, a senha e
        // a lista de autoridades (roles) do usuário.
        // A classe AuthorityUtils do Spring Security é usada para criar a lista de
        // autoridades a partir do nome do role do usuário.
        super(usuario.getUsername(), usuario.getPassword(),
                AuthorityUtils.createAuthorityList(usuario.getRole().name()));
        // Armazena o objeto Usuario no atributo usuario.
        this.usuario = usuario;
    }

    // Método que retorna o ID do usuário.
    // Este método pode ser usado para obter o ID do usuário autenticado.
    public long getId() {
        return this.usuario.getId();
    }

    // Método que retorna a função (role) do usuário.
    // Este método pode ser usado para obter a função do usuário autenticado.
    public String getRole() {
        return this.usuario.getRole().name();
    }
}
