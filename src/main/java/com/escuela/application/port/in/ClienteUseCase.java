package com.escuela.application.port.in;

import com.escuela.application.dto.ClienteDTO;
import com.escuela.application.dto.ClienteDtoResponse;

import java.util.List;

/**
 * Puerto de ENTRADA: casos de uso del ABM de clientes.
 * Firman sus metodos con DTOs (entrada/salida), no con entidades de dominio.
 * El adaptador REST (ClienteController) consume esta interfaz.
 */
public interface ClienteUseCase {

    /** Crea un cliente nuevo (409 si el email esta duplicado). */
    ClienteDtoResponse crear(ClienteDTO dto);

    /** Obtiene un cliente por id (404 si no existe). */
    ClienteDtoResponse obtenerPorId(Long id);

    /** Lista todos los clientes. */
    List<ClienteDtoResponse> listarTodos();

    /** Lista clientes por estado: ACTIVO / INACTIVO. */
    List<ClienteDtoResponse> listarPorEstado(String estado);

    /** Actualiza un cliente existente (404 si no existe, 409 si el email queda duplicado). */
    ClienteDtoResponse actualizar(Long id, ClienteDTO dto);

    /** Elimina un cliente por id (404 si no existe). */
    void eliminar(Long id);
}
