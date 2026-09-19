package com.escuela.domain.exception;

/**
 * Excepcion de dominio: el email ya esta registrado.
 * El adaptador de entrada (GlobalExceptionHandler) la mapea a HTTP 409 Conflict.
 */
public class EmailDuplicadoException extends RuntimeException {

    public EmailDuplicadoException(String mensaje) {
        super(mensaje);
    }
}
