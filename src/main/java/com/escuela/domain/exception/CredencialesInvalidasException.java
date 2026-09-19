package com.escuela.domain.exception;

/**
 * Excepcion de dominio: email o contrasena incorrectos en el login.
 * El adaptador de entrada (GlobalExceptionHandler) la mapea a HTTP 401 Unauthorized.
 */
public class CredencialesInvalidasException extends RuntimeException {

    public CredencialesInvalidasException(String mensaje) {
        super(mensaje);
    }
}
