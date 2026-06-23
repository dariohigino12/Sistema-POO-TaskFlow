package com.taskflow.service;

import com.taskflow.exception.CampoInvalidoException;
import com.taskflow.exception.CredenciaisInvalidasException;
import com.taskflow.exception.EmailJaCadastradoException;
import com.taskflow.exception.UsuarioNaoEncontradoException;
import com.taskflow.model.Usuario;
import com.taskflow.repository.UsuarioRepository;

import java.util.regex.Pattern;

/**
 * Camada de regras de negócio relacionadas a usuários: cadastro, validação
 * de dados e autenticação (login).
 */
public class UsuarioService {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$");
    private static final int TAMANHO_MINIMO_SENHA = 4;

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Cadastra um novo usuário, validando nome, e-mail e senha antes de persistir.
     */
    public Usuario cadastrar(String nome, String email, String senha) throws EmailJaCadastradoException {
        validarNome(nome);
        validarEmail(email);
        validarSenha(senha);
        return usuarioRepository.salvar(nome.trim(), email.trim().toLowerCase(), senha);
    }

    /**
     * Realiza o login, validando e-mail e senha. Por segurança, tanto o
     * "e-mail não encontrado" quanto a "senha incorreta" retornam a mesma
     * mensagem genérica de credenciais inválidas.
     */
    public Usuario login(String email, String senha) throws CredenciaisInvalidasException {
        try {
            Usuario usuario = usuarioRepository.buscarPorEmail(email.trim().toLowerCase());
            if (!usuario.autenticar(senha)) {
                throw new CredenciaisInvalidasException();
            }
            return usuario;
        } catch (UsuarioNaoEncontradoException e) {
            throw new CredenciaisInvalidasException();
        }
    }

    private void validarNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new CampoInvalidoException("O nome não pode estar vazio.");
        }
    }

    private void validarEmail(String email) {
        if (email == null || !EMAIL_PATTERN.matcher(email.trim()).matches()) {
            throw new CampoInvalidoException("E-mail inválido.");
        }
    }

    private void validarSenha(String senha) {
        if (senha == null || senha.length() < TAMANHO_MINIMO_SENHA) {
            throw new CampoInvalidoException(
                    "A senha deve ter ao menos " + TAMANHO_MINIMO_SENHA + " caracteres.");
        }
    }
}
