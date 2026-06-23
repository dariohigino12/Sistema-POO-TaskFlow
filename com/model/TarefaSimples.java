package com.taskflow.model;

/**
 * Representa uma tarefa simples, sem recorrência ou prazo.
 * É uma especialização de {@link Tarefa} que adiciona um rótulo descritivo.
 *
 * <p>Conceitos de POO: herança (extends Tarefa), polimorfismo (sobrescrita
 * de métodos abstratos calcularPeso, concluir e toString).</p>
 */
public class TarefaSimples extends Tarefa {

    private String rotulo;

    public TarefaSimples(int id, int idUsuario, String titulo, String descricao,
                         Prioridade prioridade, String rotulo) {
        super(id, idUsuario, titulo, descricao, prioridade);
        this.rotulo = rotulo;
    }

    public String getRotulo() {
        return rotulo;
    }

    public void setRotulo(String rotulo) {
        this.rotulo = rotulo;
    }

    @Override
    public void concluir() {
        super.concluir();
    }

    @Override
    public int calcularPeso() {
        return getPrioridade().getPeso();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("#%-3d [Simples   ] [%-9s] (%-5s) %s",
                getId(), getStatus().getDescricao(), getPrioridade().getDescricao(), getTitulo()));
        if (rotulo != null && !rotulo.isBlank()) {
            sb.append(" | Rótulo: ").append(rotulo);
        }
        sb.append("\n      Descrição: ")
          .append((getDescricao() == null || getDescricao().isBlank()) ? "(sem descrição)" : getDescricao());
        sb.append("\n      Criada em: ").append(getDataCriacao());
        if (getDataConclusao() != null) {
            sb.append(" | Concluída em: ").append(getDataConclusao());
        }
        return sb.toString();
    }
}
