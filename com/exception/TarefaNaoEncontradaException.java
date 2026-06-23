package com.taskflow.exception;

/**
 * Lançada quando uma tarefa com o id informado não é encontrada.
 */
public class TarefaNaoEncontradaException extends Exception {

    public TarefaNaoEncontradaException(int id) {
        super("Tarefa com id " + id + " não foi encontrada.");
    }
}
