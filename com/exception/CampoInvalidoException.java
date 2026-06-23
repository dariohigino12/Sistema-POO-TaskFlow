package com.taskflow.exception;

/**
 * Lançada quando um campo de entrada (nome, e-mail, senha, título, etc.)
 * não atende às regras de validação do sistema.
 * É uma RuntimeException pois representa um erro de uso/validação,
 * tratado de forma centralizada nas camadas de serviço/apresentação.
 */
public class CampoInvalidoException extends RuntimeException {

    public CampoInvalidoException(String mensagem) {
        super(mensagem);
    }
}
