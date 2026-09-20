package com.banco.domain.exception;

/**
 * Excepcion de dominio: el numero de cuenta ya esta registrado.
 * El adaptador de entrada (GlobalExceptionHandler) la mapea a HTTP 409 Conflict.
 */
public class NumeroCuentaDuplicadoException extends RuntimeException {

    public NumeroCuentaDuplicadoException(String mensaje) {
        super(mensaje);
    }
}