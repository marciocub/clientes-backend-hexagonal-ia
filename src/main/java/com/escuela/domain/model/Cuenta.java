package com.escuela.domain.model;

import java.math.BigDecimal;

import com.escuela.domain.Enum.EstadoCuenta;
import com.escuela.domain.Enum.Moneda;

public class Cuenta {

    private Long id;
    private String numeroCuenta;
    private Long clienteId;
    private BigDecimal saldo;
    private Moneda moneda;
    private EstadoCuenta estado;
    
    public Cuenta(){
    	this.estado = EstadoCuenta.INACTIVO;
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
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
	     this.id = id;
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
