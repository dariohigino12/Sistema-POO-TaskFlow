package com.taskflow.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Representa um usuário cadastrado no sistema TaskFlow.
 */
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    private final int id;
    private String nome;
    private String email;
    private String senha;

    public Usuario(int id, String nome, String email, String senha) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    /**
     * Verifica se a senha informada corresponde à senha cadastrada.
     */
    public boolean autenticar(String senhaTentativa) {
        return this.senha != null && this.senha.equals(senhaTentativa);
    }

    @Override
    public String toString() {
        return String.format("Usuario[id=%d, nome=%s, email=%s]", id, nome, email);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario)) return false;
        Usuario usuario = (Usuario) o;
        return id == usuario.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
