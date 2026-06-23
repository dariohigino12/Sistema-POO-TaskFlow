package com.taskflow.repository;

import com.taskflow.exception.TarefaNaoEncontradaException;
import com.taskflow.model.StatusTarefa;
import com.taskflow.model.TarefaComPrazo;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Responsável por armazenar e gerenciar as tarefas com prazo em memória.
 * Estrutura similar ao {@link TarefaRepository}, mas específica para
 * {@link TarefaComPrazo} (classe independente, não herda de Tarefa).
 */
public class TarefaComPrazoRepository {

    private final Map<Integer, TarefaComPrazo> tarefas = new LinkedHashMap<>();
    private final AtomicInteger sequencial = new AtomicInteger(1);

    public TarefaComPrazo salvar(TarefaComPrazo tarefa) {
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

    public TarefaComPrazo buscarPorId(int id) throws TarefaNaoEncontradaException {
        TarefaComPrazo tarefa = tarefas.get(id);
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

    public List<TarefaComPrazo> listarPorUsuario(int idUsuario) {
        return tarefas.values().stream()
                .filter(t -> t.getIdUsuario() == idUsuario)
                .toList();
    }

    public List<TarefaComPrazo> listarPorUsuarioEStatus(int idUsuario, StatusTarefa status) {
        return listarPorUsuario(idUsuario).stream()
                .filter(t -> t.getStatus() == status)
                .toList();
    }

    public List<TarefaComPrazo> listarAtrasadas(int idUsuario) {
        return listarPorUsuario(idUsuario).stream()
                .filter(TarefaComPrazo::isAtrasada)
                .toList();
    }

    public List<TarefaComPrazo> listarPorUsuarioOrdenadoPorPeso(int idUsuario) {
        return listarPorUsuario(idUsuario).stream()
                .sorted(Comparator.comparingInt(TarefaComPrazo::calcularPeso).reversed())
                .toList();
    }
}
