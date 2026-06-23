package com.taskflow.exception;

/**
 * Lançada quando um usuário tenta manipular uma tarefa que não é dele.
 */
public class AcessoNaoAutorizadoException extends Exception {

    public AcessoNaoAutorizadoException() {
        super("Você não tem permissão para acessar esta tarefa.");
    }
}
