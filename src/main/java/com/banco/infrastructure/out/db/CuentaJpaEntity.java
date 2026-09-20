package com.banco.infrastructure.out.db;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.math.BigDecimal;

/**
 * Unica clase de cuenta con anotaciones JPA (adaptador de salida a BD).
 * El modelo de dominio (domain/model/Cuenta) permanece puro.
 * Tabla: cuentas (numero_cuenta unico).
 */
@Entity
@Table(name = "cuentas",
        uniqueConstraints = @UniqueConstraint(name = "uk_cuenta_numero", columnNames = "numero_cuenta"))
public class CuentaJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_cuenta", nullable = false, length = 30)
    private String numeroCuenta;

    /** Cliente duenio de la cuenta (FK logica por id simple). */
    @Column(name = "cliente_id", nullable = false)
    private Long clienteId;

    @Column(name = "saldo", nullable = false, precision = 15, scale = 2)
    private BigDecimal saldo;

    /** PESOS / DOLAR / EURO / REAL (string en la tabla, enum Moneda en el dominio). */
    @Column(name = "moneda", nullable = false, length = 10)
    private String moneda;

    /** ACTIVO / INACTIVO (string en la tabla, enum EstadoCuenta en el dominio). */
    @Column(name = "estado", nullable = false, length = 20)
    private String estado;

    public CuentaJpaEntity() {
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

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}