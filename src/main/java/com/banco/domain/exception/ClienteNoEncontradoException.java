package com.banco.domain.exception;

/**
 * Excepcion de dominio: no se encontro el cliente buscado.
 * El adaptador de entrada (GlobalExceptionHandler) la mapea a HTTP 404 Not Found.
 */
public class ClienteNoEncontradoException extends RuntimeException {

    public ClienteNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
