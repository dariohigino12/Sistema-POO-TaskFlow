package com.taskflow.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Representa um lembrete associado a um usuário no sistema TaskFlow.
 * Classe independente que armazena uma mensagem e uma data/hora de alerta.
 *
 * <p>Conceitos de POO: encapsulamento (dados e comportamento juntos),
 * imutabilidade parcial (id é final).</p>
 */
public class Lembrete {

    private static final DateTimeFormatter FORMATADOR = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final int id;
    private final int idUsuario;
    private String mensagem;
    private LocalDateTime dataHora;

    public Lembrete(int id, int idUsuario, String mensagem, LocalDateTime dataHora) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.mensagem = mensagem;
        this.dataHora = dataHora;
    }

    public int getId() {
        return id;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    @Override
    public String toString() {
        return String.format("#%-3d Lembrete: %s | Em: %s",
                id, mensagem, dataHora != null ? dataHora.format(FORMATADOR) : "(sem data)");
    }
}
