package com.taskflow.model;

/**
 * Representa o status atual de uma tarefa.
 */
public enum StatusTarefa {
    PENDENTE("Pendente"),
    CONCLUIDA("Concluída");

    private final String descricao;

    StatusTarefa(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
