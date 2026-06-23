package com.taskflow.model;

/**
 * Representa os níveis de prioridade que uma tarefa pode ter.
 * O "peso" é utilizado para ordenar as tarefas (maior peso = maior prioridade).
 */
public enum Prioridade {
    ALTA(3, "Alta"),
    MEDIA(2, "Média"),
    BAIXA(1, "Baixa");

    private final int peso;
    private final String descricao;

    Prioridade(int peso, String descricao) {
        this.peso = peso;
        this.descricao = descricao;
    }

    public int getPeso() {
        return peso;
    }

    public String getDescricao() {
        return descricao;
    }
}
