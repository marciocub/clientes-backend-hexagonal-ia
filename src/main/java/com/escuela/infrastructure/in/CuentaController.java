// ==========================================================================
// MODULO CUENTA - CLASE EN CONSTRUCCION (COMENTADA SEGUN INSTRUCCION)
// Motivo: no compila - tipos inexistentes (CuetanDtoResponse,
// CuentaResponseDTO, CuentaResponseDto, Lista) y sintaxis invalida
// en CuentaController; ademas faltan CuentaService / CuentaOutPort /
// CuentaJpaEntity / CuentaRepository / CuentaAdapter.
// Descomentar este bloque cuando el modulo Cuenta este completo.
// ==========================================================================
<<<<<<< HEAD
 package com.escuela.infrastructure.in;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.escuela.application.port.out.CuentaResponseDto;

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
=======
// package com.escuela.infrastructure.in;
//
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;
//
// import com.escuela.application.port.in.CuentaUseCase;
// import com.escuela.application.port.out.CuentaResponseDto;
//
// @RestController
// @RequestMapping ("/api/cuenta")
// public class CuentaController {
//     /*crear un abm rest */
//
//     private final CuentaUseCase cuentaUseCase;
//
//     public CuentaController(CuentaUseCase cuentaUseCase) {
// 		this.cuentaUseCase = cuentaUseCase;
// 	}
//
//
//     /*listar todas la cuentas*/
//     @GetMapping
//     Lista<CuentaResponseDto>listarTodos(CuentaDTO dto){
//
//     	return List<CuentaResponseDto> cuentaUseCase.listarTodos();
//     }
//
//
// }
//
>>>>>>> 473046613a86a284a12f04fa082ce9c64fdd6e36
