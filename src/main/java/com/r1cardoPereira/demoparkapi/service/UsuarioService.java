package com.r1cardoPereira.demoparkapi.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.r1cardoPereira.demoparkapi.entity.Usuario;
import com.r1cardoPereira.demoparkapi.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

/**
 * Classe de serviço para operações relacionadas ao usuário.
 */
@RequiredArgsConstructor
@Service
public class UsuarioService {

    /**
     * Repositório de usuários, usado para interagir com o banco de dados.
     */
    private final UsuarioRepository usuarioRepository;

    /**
     * Salva um usuário no banco de dados.
     * 
     * @param usuario - O usuário a ser salvo.
     * @return O usuário salvo.
     */
    @Transactional
    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    /**
     * Busca um usuário pelo ID.
     * 
     * @param id - O ID do usuário a ser buscado.
     * @return O usuário encontrado.
     * @throws RuntimeException se o usuário não for encontrado.
     */
    @Transactional(readOnly = true)
    public Usuario getByid(Long id) {
        return usuarioRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Usuário não encontrado!"));
    }

    /**
     * Atualiza a senha de um usuário.
     * 
     * @param id            - O ID do usuário a ter a senha atualizada.
     * @param senhaAtual    - A senha atual do usuário.
     * @param novaSenha     - A nova senha que o usuário deseja definir.
     * @param confirmaSenha - A confirmação da nova senha.
     * @return O usuário com a senha atualizada.
     * @throws RuntimeException se a nova senha e a confirmação da senha não forem iguais ou se a senha atual for inválida.
     */
    @Transactional
    public Usuario updatePassword(Long id, String senhaAtual, String novaSenha, String confirmaSenha) {
        if (!novaSenha.equals(confirmaSenha)) {
            throw new RuntimeException("As senhas não conferem.");
        }
        Usuario user = getByid(id);
        if (!user.getPassword().equals(senhaAtual)) {
            throw new RuntimeException("Senha invalida!");
        }

        user.setPassword(novaSenha);
        return user;
    }

    /**
     * Busca todos os usuários.
     * 
     * @return Uma lista de todos os usuários.
     */
    @Transactional(readOnly = true)
    public List<Usuario> getAll() {
        return usuarioRepository.findAll();
    }
}