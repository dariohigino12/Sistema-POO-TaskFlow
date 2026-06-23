package com.taskflow.model;

import java.time.LocalDate;

/**
 * Representa uma tarefa recorrente, que se repete em intervalos regulares.
 * É uma especialização de {@link Tarefa} que adiciona frequência em dias
 * e controle da próxima ocorrência.
 *
 * <p>Conceitos de POO: herança (extends Tarefa), polimorfismo (sobrescrita
 * de métodos abstratos calcularPeso, concluir e toString), encapsulamento
 * (calcularProximaOcorrencia é chamado automaticamente ao concluir).</p>
 */
public class TarefaRecorrente extends Tarefa {

    private int frequenciaDias;
    private LocalDate dataProximaOcorrencia;

    public TarefaRecorrente(int id, int idUsuario, String titulo, String descricao,
                            Prioridade prioridade, int frequenciaDias) {
        super(id, idUsuario, titulo, descricao, prioridade);
        if (frequenciaDias <= 0) {
            throw new IllegalArgumentException("A frequência em dias deve ser positiva.");
        }
        this.frequenciaDias = frequenciaDias;
        this.dataProximaOcorrencia = LocalDate.now().plusDays(frequenciaDias);
    }

    public int getFrequenciaDias() {
        return frequenciaDias;
    }

    public void setFrequenciaDias(int frequenciaDias) {
        if (frequenciaDias <= 0) {
            throw new IllegalArgumentException("A frequência em dias deve ser positiva.");
        }
        this.frequenciaDias = frequenciaDias;
    }

    public LocalDate getDataProximaOcorrencia() {
        return dataProximaOcorrencia;
    }

    /**
     * Calcula e atualiza a próxima data de ocorrência com base na frequência.
     * Chamado automaticamente ao concluir a tarefa.
     */
    public void calcularProximaOcorrencia() {
        this.dataProximaOcorrencia = LocalDate.now().plusDays(frequenciaDias);
    }

    @Override
    public void concluir() {
        super.concluir();
        calcularProximaOcorrencia();
    }

    @Override
    public int calcularPeso() {
        // Peso base + bônus proporcional à frequência (quanto mais frequente, maior o peso)
        int pesoBase = getPrioridade().getPeso();
        int bonusFrequencia = (frequenciaDias <= 7) ? 2 : (frequenciaDias <= 30) ? 1 : 0;
        return pesoBase + bonusFrequencia;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("#%-3d [Recorrente] [%-9s] (%-5s) %s",
                getId(), getStatus().getDescricao(), getPrioridade().getDescricao(), getTitulo()));
        sb.append(" | A cada ").append(frequenciaDias).append(" dia(s)");
        sb.append("\n      Descrição: ")
          .append((getDescricao() == null || getDescricao().isBlank()) ? "(sem descrição)" : getDescricao());
        sb.append("\n      Criada em: ").append(getDataCriacao());
        sb.append(" | Próxima ocorrência: ").append(dataProximaOcorrencia);
        if (getDataConclusao() != null) {
            sb.append(" | Concluída em: ").append(getDataConclusao());
        }
        return sb.toString();
    }
}
