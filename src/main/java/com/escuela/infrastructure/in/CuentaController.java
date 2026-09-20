// ==========================================================================
// MODULO CUENTA - CLASE EN CONSTRUCCION (COMENTADA SEGUN INSTRUCCION)
// Motivo: no compila - tipos inexistentes (CuetanDtoResponse,
// CuentaResponseDTO, CuentaResponseDto, Lista) y sintaxis invalida
// en CuentaController; ademas faltan CuentaService / CuentaOutPort /
// CuentaJpaEntity / CuentaRepository / CuentaAdapter.
// Descomentar este bloque cuando el modulo Cuenta este completo.
// ==========================================================================
 package com.escuela.infrastructure.in;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.escuela.application.dto.CuentaDTO;
import com.escuela.application.port.in.CuentaUseCase;

 @RestController
 @RequestMapping ("/api/cuenta")
 public class CuentaController {
//     /*crear un abm rest */
//
     private final CuentaUseCase cuentaUseCase;

     public CuentaController(CuentaUseCase cuentaUseCase) {
 		this.cuentaUseCase = cuentaUseCase;
 	}


     /*listar todas la cuentas*/
     @GetMapping
     Lista<CuentaDtoResponse>listarTodos(CuentaDTO dto){

     	return List<CuentaDtoResponse> cuentaUseCase.listarTodos();
     }


 }
