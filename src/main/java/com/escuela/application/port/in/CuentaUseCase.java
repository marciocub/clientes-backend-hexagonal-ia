package com.escuela.application.port.in;

import java.util.List;

import com.escuela.application.dto.AlumnoDtoResponse;
import com.escuela.application.dto.CuentaDTO;
import com.escuela.application.port.out.CuentaDtoResponse;

/**
 * Puerto de ENTRADA: casos de uso del ABM de alumnos.
 * Firman sus metodos con DTOs (entrada/salida), no con entidades de dominio.
 * El adaptador REST (AlumnoController) consume esta interfaz.
 */
public interface CuentaUseCase {

	void crear(CuentaDTO dto);
	
    /** Obtiene un alumno por id (404 si no existe). */
    CuetanDtoResponse obtenerPorId(Long id);

    /** Lista todos los alumnos. */
    List<CuentaDtoResponse> listarTodos();
    
    CuentaResponseDTO actualizar(Long id ,CuentaDTO dto);
     
    void eliminar(Long id);
    
  
    
}
