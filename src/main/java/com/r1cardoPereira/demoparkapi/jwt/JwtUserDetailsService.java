package com.r1cardoPereira.demoparkapi.jwt;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.r1cardoPereira.demoparkapi.entity.Usuario;
import com.r1cardoPereira.demoparkapi.service.UsuarioService;

import lombok.RequiredArgsConstructor;

@Service // Anotação que indica que esta classe é um serviço no Spring.
@RequiredArgsConstructor // Anotação do Lombok que gera um construtor com um parâmetro para cada campo
                         // final na classe.

public class JwtUserDetailsService implements UserDetailsService { // Declaração da classe que implementa a interface
                                                                   // UserDetailsService do Spring Security.

    private final UsuarioService usuarioService; // Injeção de dependência do serviço de usuários.

    @Override // Indica que este método está sobrescrevendo um método da interface
              // UserDetailsService.
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { // Método que carrega um
                                                                                              // usuário pelo nome de
                                                                                              // usuário.

        Usuario usuario = usuarioService.buscarPorUsername(username); // Busca o usuário pelo nome de usuário.

        return new JwtUserDetails(usuario); // Retorna uma nova instância de JwtUserDetails com o usuário encontrado.
    }

    public JwtToken getTokenAuthenticated(String username) { // Método que retorna um token JWT para um usuário
                                                             // autenticado.

        Usuario.Role role = usuarioService.buscarRolePorUsername(username); // Busca a função (role) do usuário pelo
                                                                            // nome de usuário.

        return JwtUtils.createToken(username, role.name().substring("ROLE_".length())); // Cria um token JWT com o nome
                                                                                        // de usuário e a função do
                                                                                        // usuário (sem o prefixo
                                                                                        // "ROLE_").
    }

}
