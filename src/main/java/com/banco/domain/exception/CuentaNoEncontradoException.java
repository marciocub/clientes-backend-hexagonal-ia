package com.banco.domain.exception;

/**
 * Excepcion de dominio: no se encontro la cuenta buscada.
 * El adaptador de entrada (GlobalExceptionHandler) la mapea a HTTP 404 Not Found.
 */
public class CuentaNoEncontradoException extends RuntimeException {

    public CuentaNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}