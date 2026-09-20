package com.banco.application.port.in;

import java.util.List;

import com.banco.application.dto.CuentaDTO;
import com.banco.application.dto.CuentaDtoResponse;

/**
 * Puerto de ENTRADA: casos de uso del ABM de cuentas.
 * Firman sus metodos con DTOs (entrada/salida), no con entidades de dominio.
 * El adaptador REST (CuentaController) consume esta interfaz.
 */
public interface CuentaUseCase {

    /** Crea una cuenta nueva (409 si el numero de cuenta esta duplicado). */
    CuentaDtoResponse crear(CuentaDTO dto);

    /** Obtiene una cuenta por id (404 si no existe). */
    CuentaDtoResponse obtenerPorId(Long id);

    /** Lista todas las cuentas. */
    List<CuentaDtoResponse> listarTodos();

    /** Lista cuentas por estado: ACTIVO / INACTIVO. */
    List<CuentaDtoResponse> listarPorEstado(String estado);

    /** Actualiza una cuenta existente (404 si no existe, 409 si el numero queda duplicado). */
    CuentaDtoResponse actualizar(Long id, CuentaDTO dto);

    /** Elimina una cuenta por id (404 si no existe). */
    void eliminar(Long id);
}