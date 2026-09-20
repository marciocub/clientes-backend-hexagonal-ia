package com.banco.domain.exception;

/**
 * Excepcion de dominio: un consumo excede el credito disponible de la tarjeta.
 * El adaptador de entrada (GlobalExceptionHandler) la mapea a HTTP 409 Conflict.
 */
public class ConsumoExcedeDisponibleException extends RuntimeException {

    public ConsumoExcedeDisponibleException(String mensaje) {
        super(mensaje);
    }
}