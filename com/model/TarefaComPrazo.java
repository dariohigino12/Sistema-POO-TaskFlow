package com.taskflow.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Representa uma tarefa com prazo de entrega definido.
 * Esta é uma classe independente (não herda de {@link Tarefa}),
 * possuindo seus próprios campos e comportamentos.
 *
 * <p>Conceitos de POO: encapsulamento (isAtrasada calcula internamente),
 * composição (usa Prioridade e StatusTarefa), coesão (todos os dados
 * e comportamentos de tarefa com prazo estão nesta classe).</p>
 */
public class TarefaComPrazo {

    private static final DateTimeFormatter FORMATADOR = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final int id;
    private final int idUsuario;
    private String titulo;
    private String descricao;
    private final LocalDate dataCriacao;
    private LocalDateTime dataConclusao;
    private StatusTarefa status;
    private Prioridade prioridade;
    private LocalDate dataLimite;

    public TarefaComPrazo(int id, int idUsuario, String titulo, String descricao,
                          Prioridade prioridade, LocalDate dataLimite) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.titulo = titulo;
        this.descricao = descricao;
        this.prioridade = prioridade;
        this.dataLimite = dataLimite;
        this.dataCriacao = LocalDate.now();
        this.status = StatusTarefa.PENDENTE;
    }

    public int getId() {
        return id;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public LocalDateTime getDataConclusao() {
        return dataConclusao;
    }

    public StatusTarefa getStatus() {
        return status;
    }

    public Prioridade getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(Prioridade prioridade) {
        this.prioridade = prioridade;
    }

    public LocalDate getDataLimite() {
        return dataLimite;
    }

    public void setDataLimite(LocalDate dataLimite) {
        this.dataLimite = dataLimite;
    }

    public boolean isConcluida() {
        return status == StatusTarefa.CONCLUIDA;
    }

    /**
     * Verifica se a tarefa está atrasada: prazo vencido e ainda não concluída.
     */
    public boolean isAtrasada() {
        return !isConcluida() && dataLimite != null && LocalDate.now().isAfter(dataLimite);
    }

    public void concluir() {
        this.status = StatusTarefa.CONCLUIDA;
        this.dataConclusao = LocalDateTime.now();
    }

    public void reabrir() {
        this.status = StatusTarefa.PENDENTE;
        this.dataConclusao = null;
    }

    /**
     * Calcula o peso da tarefa considerando prioridade e proximidade do prazo.
     * Tarefas atrasadas recebem bônus de urgência.
     */
    public int calcularPeso() {
        int pesoBase = prioridade.getPeso();
        if (isAtrasada()) {
            return pesoBase + 3; // bônus máximo para atrasadas
        }
        if (dataLimite != null && !isConcluida()) {
            long diasRestantes = LocalDate.now().until(dataLimite).getDays();
            if (diasRestantes <= 1) return pesoBase + 2;
            if (diasRestantes <= 3) return pesoBase + 1;
        }
        return pesoBase;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("#%-3d [%-9s] (%-5s) %s",
                id, status.getDescricao(), prioridade.getDescricao(), titulo));
        if (isAtrasada()) {
            sb.append(" ⚠ ATRASADA");
        }
        sb.append("\n      Descrição: ")
          .append((descricao == null || descricao.isBlank()) ? "(sem descrição)" : descricao);
        sb.append("\n      Criada em: ").append(dataCriacao.format(FORMATADOR));
        sb.append(" | Prazo: ").append(dataLimite != null ? dataLimite.format(FORMATADOR) : "(sem prazo)");
        if (dataConclusao != null) {
            sb.append(" | Concluída em: ").append(dataConclusao.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        }
        return sb.toString();
    }
}
