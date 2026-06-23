package com.taskflow.model;

import java.time.LocalDate;

/**
 * Classe abstrata que representa uma tarefa genérica no sistema TaskFlow.
 * Serve como superclasse para {@link TarefaSimples} e {@link TarefaRecorrente},
 * definindo os atributos e comportamentos comuns a todos os tipos de tarefa.
 *
 * <p>Conceitos de POO aplicados:</p>
 * <ul>
 *   <li><b>Abstração:</b> classe não pode ser instanciada diretamente</li>
 *   <li><b>Herança:</b> subclasses herdam campos e métodos concretos</li>
 *   <li><b>Polimorfismo:</b> método abstrato {@code calcularPeso()} é implementado
 *       diferentemente em cada subclasse</li>
 *   <li><b>Encapsulamento:</b> transições de estado ({@code concluir}/{@code reabrir})
 *       são controladas internamente</li>
 *   <li><b>Composição:</b> utiliza {@link Prioridade} e {@link StatusTarefa}</li>
 * </ul>
 */
public abstract class Tarefa {

    private final int id;
    private final int idUsuario;
    private String titulo;
    private String descricao;
    private Prioridade prioridade;
    private final LocalDate dataCriacao;
    private LocalDate dataConclusao;
    private StatusTarefa status;

    protected Tarefa(int id, int idUsuario, String titulo, String descricao, Prioridade prioridade) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.titulo = titulo;
        this.descricao = descricao;
        this.prioridade = prioridade;
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

    public Prioridade getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(Prioridade prioridade) {
        this.prioridade = prioridade;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public LocalDate getDataConclusao() {
        return dataConclusao;
    }

    protected void setDataConclusao(LocalDate dataConclusao) {
        this.dataConclusao = dataConclusao;
    }

    public StatusTarefa getStatus() {
        return status;
    }

    protected void setStatus(StatusTarefa status) {
        this.status = status;
    }

    /** Marca a tarefa como concluída, registrando a data da conclusão. */
    public void concluir() {
        this.status = StatusTarefa.CONCLUIDA;
        this.dataConclusao = LocalDate.now();
    }

    /** Reabre a tarefa, voltando o status para pendente. */
    public void reabrir() {
        this.status = StatusTarefa.PENDENTE;
        this.dataConclusao = null;
    }

    public boolean isConcluida() {
        return status == StatusTarefa.CONCLUIDA;
    }

    /**
     * Calcula o peso da tarefa para ordenação.
     * Cada subclasse implementa sua própria lógica de cálculo.
     *
     * @return peso da tarefa (quanto maior, maior a prioridade na listagem)
     */
    public abstract int calcularPeso();

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("#%-3d [%-9s] (%-5s) %s",
                id, status.getDescricao(), prioridade.getDescricao(), titulo));
        sb.append("\n      Descrição: ")
          .append((descricao == null || descricao.isBlank()) ? "(sem descrição)" : descricao);
        sb.append("\n      Criada em: ").append(dataCriacao);
        if (dataConclusao != null) {
            sb.append(" | Concluída em: ").append(dataConclusao);
        }
        return sb.toString();
    }
}
