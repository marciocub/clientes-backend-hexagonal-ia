package com.escuela.infrastructure.in;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.escuela.application.port.in.CuentaUseCase;
import com.escuela.application.port.out.CuentaResponseDto;

@RestController
@RequestMapping ("/api/cuenta")
public class CuentaController {
    /*crear un abm rest */

    private final CuentaUseCase cuentaUseCase;
    
    public CuentaController(CuentaUseCase cuentaUseCase) {
		this.cuentaUseCase = cuentaUseCase;
	}
    
    
    /*listar todas la cuentas*/
    @GetMapping
    Lista<CuentaResponseDto>listarTodos(CuentaDTO dto){
    	
    	return List<CuentaResponseDto> cuentaUseCase.listarTodos();
    }

    
}
