package com.escuela.application.port.in;

import com.escuela.application.dto.AlumnoDTO;
import com.escuela.application.dto.AlumnoDtoResponse;

import java.util.List;

/**
 * Puerto de ENTRADA: casos de uso del ABM de alumnos.
 * Firman sus metodos con DTOs (entrada/salida), no con entidades de dominio.
 * El adaptador REST (AlumnoController) consume esta interfaz.
 */
public interface AlumnoUseCase {

    /** Crea un alumno nuevo (409 si el email esta duplicado). */
    AlumnoDtoResponse crear(AlumnoDTO dto);

    /** Obtiene un alumno por id (404 si no existe). */
    AlumnoDtoResponse obtenerPorId(Long id);

    /** Lista todos los alumnos. */
    List<AlumnoDtoResponse> listarTodos();

    /** Lista alumnos por estado: ACTIVO / INACTIVO. */
    List<AlumnoDtoResponse> listarPorEstado(String estado);

    /** Actualiza un alumno existente (404 si no existe, 409 si el email queda duplicado). */
    AlumnoDtoResponse actualizar(Long id, AlumnoDTO dto);

    /** Elimina un alumno por id (404 si no existe). */
    void eliminar(Long id);
}
