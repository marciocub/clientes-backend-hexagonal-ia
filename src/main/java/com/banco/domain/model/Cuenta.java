package com.banco.domain.model;

import java.math.BigDecimal;

import com.banco.domain.Enum.EstadoCuenta;
import com.banco.domain.Enum.Moneda;

/**
 * Modelo de negocio puro de una Cuenta.
 *
 * Regla hexagonal: SIN anotaciones JPA (@Entity, @Table, @Column),
 * SIN imports de Spring ni Lombok. Solo negocio.
 *
 * El id es inmutable (final): lo genera la base de datos y se asigna
 * una sola vez por constructor (lo usa el mapper JPA al leer).
 */
public class Cuenta {

    private final Long id;
    private String numeroCuenta;
    private Long clienteId;
    private BigDecimal saldo;
    private Moneda moneda;
    private EstadoCuenta estado;

    /** Constructor completo: lo usan el service y el mapper JPA para reconstruir el id. */
    public Cuenta(Long id, String numeroCuenta, Long clienteId, BigDecimal saldo,
                  Moneda moneda, EstadoCuenta estado) {
        this.id = id;
        this.numeroCuenta = numeroCuenta;
        this.clienteId = clienteId;
        this.saldo = saldo;
        this.moneda = moneda;
        this.estado = estado;
    }

    /** Constructor vacio: default de negocio, estado INACTIVO. */
    public Cuenta() {
        this(null, null, null, null, null, EstadoCuenta.INACTIVO);
    }

    public Long getId() {
        return id;
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