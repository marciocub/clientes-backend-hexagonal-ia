// ==========================================================================
// MODULO CUENTA - CLASE EN CONSTRUCCION (COMENTADA SEGUN INSTRUCCION)
// Motivo: no compila - tipos inexistentes (CuetanDtoResponse,
// CuentaResponseDTO, CuentaResponseDto, Lista) y sintaxis invalida
// en CuentaController; ademas faltan CuentaService / CuentaOutPort /
// CuentaJpaEntity / CuentaRepository / CuentaAdapter.
// Descomentar este bloque cuando el modulo Cuenta este completo.
// ==========================================================================
 package com.escuela.application.dto;

import java.math.BigDecimal;

import com.escuela.domain.Enum.EstadoCuenta;
import com.escuela.domain.Enum.Moneda;

public class CuentaDTO {
	//nova por que no mandas el id cuendo es nuevo
    //private Long id;
    private String numeroCuenta;
    private Long clienteId;
    private BigDecimal saldo;
    private Moneda moneda;
    private EstadoCuenta estado;
    
    public CuentaDTO(){
    	
    }
    
	public BigDecimal getSaldo() {
		return saldo;
	}
	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}
	public EstadoCuenta getEstado() {
		return estado;
	}
	public void setEstado(EstadoCuenta estado) {
		this.estado = estado;
	}	
	
	public String getNumeroCuenta() {
		return numeroCuenta;
	}
	public Long getClienteId() {
		return clienteId;
	}
	public Moneda getMoneda() {
		return moneda;
	}
}
