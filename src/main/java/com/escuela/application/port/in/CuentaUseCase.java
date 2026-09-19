// ==========================================================================
// MODULO CUENTA - CLASE EN CONSTRUCCION (COMENTADA SEGUN INSTRUCCION)
// Motivo: no compila - tipos inexistentes (CuetanDtoResponse,
// CuentaResponseDTO, CuentaResponseDto, Lista) y sintaxis invalida
// en CuentaController; ademas faltan CuentaService / CuentaOutPort /
// CuentaJpaEntity / CuentaRepository / CuentaAdapter.
// Descomentar este bloque cuando el modulo Cuenta este completo.
// ==========================================================================
// package com.escuela.application.port.in;
//
// import java.util.List;
//
// import com.escuela.application.dto.ClienteDtoResponse;
// import com.escuela.application.dto.CuentaDTO;
// import com.escuela.application.port.out.CuentaDtoResponse;
//
// /**
//  * Puerto de ENTRADA: casos de uso del ABM de clientes.
//  * Firman sus metodos con DTOs (entrada/salida), no con entidades de dominio.
//  * El adaptador REST (ClienteController) consume esta interfaz.
//  */
// public interface CuentaUseCase {
//
// 	void crear(CuentaDTO dto);
//
//     /** Obtiene un cliente por id (404 si no existe). */
//     CuetanDtoResponse obtenerPorId(Long id);
//
//     /** Lista todos los clientes. */
//     List<CuentaDtoResponse> listarTodos();
//
//     CuentaResponseDTO actualizar(Long id ,CuentaDTO dto);
//
//     void eliminar(Long id);
//
//
//
// }
//