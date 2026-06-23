package com.taskflow.repository;

import com.taskflow.model.Lembrete;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Responsável por armazenar e gerenciar os lembretes em memória.
 * Cada lembrete é associado a um usuário através do idUsuario.
 */
public class LembreteRepository {

    private final Map<Integer, Lembrete> lembretes = new LinkedHashMap<>();
    private final AtomicInteger sequencial = new AtomicInteger(1);

    public Lembrete salvar(int idUsuario, String mensagem, LocalDateTime dataHora) {
        if (idUsuario <= 0) {
            throw new IllegalArgumentException("O idUsuario deve ser positivo.");
        }
        if (mensagem == null || mensagem.isBlank()) {
            throw new IllegalArgumentException("A mensagem do lembrete não pode ser vazia.");
        }
        Lembrete lembrete = new Lembrete(sequencial.getAndIncrement(), idUsuario, mensagem, dataHora);
        lembretes.put(lembrete.getId(), lembrete);
        return lembrete;
    }

    public Lembrete buscarPorId(int id) {
        Lembrete lembrete = lembretes.get(id);
        if (lembrete == null) {
            throw new IllegalArgumentException("Lembrete com id " + id + " não foi encontrado.");
        }
        return lembrete;
    }

    public void excluir(int id) {
        if (lembretes.remove(id) == null) {
            throw new IllegalArgumentException("Lembrete com id " + id + " não foi encontrado.");
        }
    }

    public List<Lembrete> listarPorUsuario(int idUsuario) {
        List<Lembrete> resultado = new ArrayList<>();
        for (Lembrete l : lembretes.values()) {
            if (l.getIdUsuario() == idUsuario) {
                resultado.add(l);
            }
        }
        return resultado;
    }
}
