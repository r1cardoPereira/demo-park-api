package com.r1cardoPereira.demoparkapi.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.r1cardoPereira.demoparkapi.entity.Usuario;
import com.r1cardoPereira.demoparkapi.exception.EntityNotFoundException;
import com.r1cardoPereira.demoparkapi.exception.PasswordInvalidException;
import com.r1cardoPereira.demoparkapi.exception.UsernameUniqueViolationException;
import com.r1cardoPereira.demoparkapi.repository.UsuarioRepository;


import lombok.RequiredArgsConstructor;

/**
 * Classe UsuarioService.
 * Esta classe é responsável por realizar operações relacionadas ao usuário.
 * Ela usa a anotação @Service para indicar que é um componente de serviço do Spring.
 * Ela usa a anotação @RequiredArgsConstructor para gerar automaticamente um construtor com um parâmetro para cada campo final na classe.
 */
@RequiredArgsConstructor
@Service
public class UsuarioService {

    /**
     * O repositório de usuários, usado para interagir com o banco de dados.
     */
    private final UsuarioRepository usuarioRepository;

    /**
     * Este método salva um usuário no banco de dados.
     * Ele usa a anotação @Transactional para indicar que é uma operação transacional.
     * 
     * @param usuario O usuário a ser salvo.
     * @return O usuário salvo.
     * @throws UsernameUniqueViolationException se o nome de usuário já estiver em uso.
     */
    @Transactional
    public Usuario save(Usuario usuario) {
        try {
            return usuarioRepository.save(usuario);
        }catch (org.springframework.dao.DataIntegrityViolationException ex){
            throw new UsernameUniqueViolationException(String.format("Username {%s} já Cadastrado", usuario.getUsername()));
        }
    }

    /**
     * Este método busca um usuário pelo ID.
     * Ele usa a anotação @Transactional(readOnly = true) para indicar que é uma operação de leitura transacional.
     * 
     * @param id O ID do usuário a ser buscado.
     * @return O usuário encontrado.
     * @throws EntityNotFoundException se o usuário não for encontrado.
     */
    @Transactional(readOnly = true)
    public Usuario getByid(Long id) {
        return usuarioRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Usuário id=%s não encontrado.", id)));
    }

    /**
     * Este método atualiza a senha de um usuário.
     * Ele usa a anotação @Transactional para indicar que é uma operação transacional.
     * 
     * @param id O ID do usuário a ter a senha atualizada.
     * @param senhaAtual A senha atual do usuário.
     * @param novaSenha A nova senha que o usuário deseja definir.
     * @param confirmaSenha A confirmação da nova senha.
     * @return O usuário com a senha atualizada.
     * @throws RuntimeException se a nova senha e a confirmação da senha não forem iguais ou se a senha atual for inválida.
     */
    @Transactional
    public Usuario updatePassword(Long id, String senhaAtual, String novaSenha, String confirmaSenha) {
        if (!novaSenha.equals(confirmaSenha)) {
            throw new PasswordInvalidException("As senhas não conferem.");
        }
        Usuario user = getByid(id);
        if (!user.getPassword().equals(senhaAtual)) {
            throw new PasswordInvalidException("Senha invalida!");
        }

        user.setPassword(novaSenha);
        return user;
    }

    /**
     * Este método busca todos os usuários.
     * Ele usa a anotação @Transactional(readOnly = true) para indicar que é uma operação de leitura transacional.
     * 
     * @return Uma lista de todos os usuários.
     */
    @Transactional(readOnly = true)
    public List<Usuario> getAll() {
        return usuarioRepository.findAll();
    }
}