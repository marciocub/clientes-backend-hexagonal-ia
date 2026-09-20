package com.banco.domain.exception;

/**
 * Excepcion de dominio: el limite total de tarjetas del cliente superaria
 * el tope de negocio ($2.000.000).
 * El adaptador de entrada (GlobalExceptionHandler) la mapea a HTTP 409 Conflict.
 */
public class LimiteTarjetasExcedidoException extends RuntimeException {

    public LimiteTarjetasExcedidoException(String mensaje) {
        super(mensaje);
    }
}