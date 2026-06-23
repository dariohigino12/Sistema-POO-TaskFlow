package com.taskflow.exception;

/**
 * Lançada quando o e-mail ou a senha informados no login estão incorretos.
 */
public class CredenciaisInvalidasException extends Exception {

    public CredenciaisInvalidasException() {
        super("E-mail ou senha inválidos.");
    }
}
