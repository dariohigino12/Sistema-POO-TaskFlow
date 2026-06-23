package com.taskflow.exception;

/**
 * Lançada ao tentar cadastrar um usuário com um e-mail já existente.
 */
public class EmailJaCadastradoException extends Exception {

    public EmailJaCadastradoException(String email) {
        super("O e-mail '" + email + "' já está cadastrado no sistema.");
    }
}
