package com.taskflow.repository;

import com.taskflow.exception.EmailJaCadastradoException;
import com.taskflow.exception.UsuarioNaoEncontradoException;
import com.taskflow.model.Usuario;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Responsável por armazenar e gerenciar os usuários em memória.
 * Centraliza o acesso aos dados, isolando a lógica de negócio (Service)
 * da forma como os dados são efetivamente guardados.
 */
public class UsuarioRepository {

    private final Map<Integer, Usuario> usuarios = new LinkedHashMap<>();
    private final AtomicInteger sequencial = new AtomicInteger(1);

    public Usuario salvar(String nome, String email, String senha) throws EmailJaCadastradoException {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do usuário não pode ser vazio.");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("O e-mail do usuário não pode ser vazio.");
        }
        if (senha == null || senha.isEmpty()) {
            throw new IllegalArgumentException("A senha do usuário não pode ser vazia.");
        }
        if (buscarPorEmailOpcional(email).isPresent()) {
            throw new EmailJaCadastradoException(email);
        }
        Usuario usuario = new Usuario(sequencial.getAndIncrement(), nome, email, senha);
        usuarios.put(usuario.getId(), usuario);
        return usuario;
    }

    public Optional<Usuario> buscarPorEmailOpcional(String email) {
        return usuarios.values().stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    public Usuario buscarPorEmail(String email) throws UsuarioNaoEncontradoException {
        return buscarPorEmailOpcional(email)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("email = " + email));
    }

    public Usuario buscarPorId(int id) throws UsuarioNaoEncontradoException {
        Usuario usuario = usuarios.get(id);
        if (usuario == null) {
            throw new UsuarioNaoEncontradoException("id = " + id);
        }
        return usuario;
    }

    public List<Usuario> listarTodos() {
        return new ArrayList<>(usuarios.values());
    }
}
