package com.escuela.application.dto;

import java.math.BigDecimal;

import com.escuela.domain.Enum.EstadoCuenta;
import com.escuela.domain.Enum.Moneda;

/**
 * DTO de SALIDA con los datos de una cuenta.
 * Nunca expone la entidad JPA ni la entidad de dominio.
 */
public class CuentaDtoResponse {

    private Long id;
    private String numeroCuenta;
    private Long clienteId;
    private BigDecimal saldo;
    private Moneda moneda;
    private EstadoCuenta estado;

    public CuentaDtoResponse() {
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

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public Moneda getMoneda() {
        return moneda;
    }

    public void setMoneda(Moneda moneda) {
        this.moneda = moneda;
    }

    public EstadoCuenta getEstado() {
        return estado;
    }

    public void setEstado(EstadoCuenta estado) {
        this.estado = estado;
    }
}