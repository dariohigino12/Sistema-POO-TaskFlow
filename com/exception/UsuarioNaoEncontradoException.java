package com.taskflow.exception;

/**
 * Lançada quando um usuário não é localizado no repositório.
 */
public class UsuarioNaoEncontradoException extends Exception {

    public UsuarioNaoEncontradoException(String detalhe) {
        super("Usuário não encontrado (" + detalhe + ").");
    }
}
