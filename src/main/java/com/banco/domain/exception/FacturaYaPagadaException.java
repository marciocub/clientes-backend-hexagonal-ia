package com.banco.domain.exception;

/**
 * Excepcion de dominio: se intento pagar una factura que ya estaba pagada.
 * El adaptador de entrada (GlobalExceptionHandler) la mapea a HTTP 409 Conflict.
 */
public class FacturaYaPagadaException extends RuntimeException {

    public FacturaYaPagadaException(String mensaje) {
        super(mensaje);
    }
}