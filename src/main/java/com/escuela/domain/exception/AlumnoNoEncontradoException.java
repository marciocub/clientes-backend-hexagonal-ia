package com.escuela.domain.exception;

/**
 * Excepcion de dominio: no se encontro el alumno buscado.
 * El adaptador de entrada (GlobalExceptionHandler) la mapea a HTTP 404 Not Found.
 */
public class AlumnoNoEncontradoException extends RuntimeException {

    public AlumnoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
