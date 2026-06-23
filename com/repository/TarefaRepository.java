package com.taskflow.repository;

import com.taskflow.exception.TarefaNaoEncontradaException;
import com.taskflow.model.StatusTarefa;
import com.taskflow.model.Tarefa;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Responsável por armazenar e gerenciar as tarefas em memória.
 * Aceita qualquer subclasse de {@link Tarefa} (polimorfismo):
 * {@link com.taskflow.model.TarefaSimples} e {@link com.taskflow.model.TarefaRecorrente}.
 *
 * <p>Oferece operações de CRUD e consultas filtradas (por usuário,
 * status e prioridade/peso).</p>
 */
public class TarefaRepository {

    private final Map<Integer, Tarefa> tarefas = new LinkedHashMap<>();
    private final AtomicInteger sequencial = new AtomicInteger(1);

    /**
     * Salva uma tarefa já construída (polimorfismo — aceita qualquer subclasse).
     * O ID é atribuído automaticamente se ainda não estiver definido.
     */
    public Tarefa salvar(Tarefa tarefa) {
        if (tarefa == null) {
            throw new IllegalArgumentException("A tarefa não pode ser nula.");
        }
        if (tarefa.getIdUsuario() <= 0) {
            throw new IllegalArgumentException("O idUsuario deve ser positivo.");
        }
        if (tarefa.getTitulo() == null || tarefa.getTitulo().isBlank()) {
            throw new IllegalArgumentException("O título da tarefa não pode ser vazio.");
        }
        if (tarefa.getPrioridade() == null) {
            throw new IllegalArgumentException("A prioridade da tarefa não pode ser nula.");
        }
        tarefas.put(tarefa.getId(), tarefa);
        return tarefa;
    }

    public int proximoId() {
        return sequencial.getAndIncrement();
    }

    public Tarefa buscarPorId(int id) throws TarefaNaoEncontradaException {
        Tarefa tarefa = tarefas.get(id);
        if (tarefa == null) {
            throw new TarefaNaoEncontradaException(id);
        }
        return tarefa;
    }

    public void excluir(int id) throws TarefaNaoEncontradaException {
        if (tarefas.remove(id) == null) {
            throw new TarefaNaoEncontradaException(id);
        }
    }

    public List<Tarefa> listarPorUsuario(int idUsuario) {
        return tarefas.values().stream()
                .filter(t -> t.getIdUsuario() == idUsuario)
                .toList();
    }

    public List<Tarefa> listarPorUsuarioEStatus(int idUsuario, StatusTarefa status) {
        return listarPorUsuario(idUsuario).stream()
                .filter(t -> t.getStatus() == status)
                .toList();
    }

    public List<Tarefa> listarPorUsuarioOrdenadoPorPeso(int idUsuario) {
        return listarPorUsuario(idUsuario).stream()
                .sorted(Comparator.comparingInt(Tarefa::calcularPeso).reversed())
                .toList();
    }
}
