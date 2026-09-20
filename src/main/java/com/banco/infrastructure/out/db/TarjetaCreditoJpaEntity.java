package com.banco.infrastructure.out.db;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Entidad JPA de una tarjeta de credito (lado propietario de la relacion).
 * Tabla: tarjetas_credito (FK cliente_id).
 */
@Entity
@Table(name = "tarjetas_credito")
public class TarjetaCreditoJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Cliente duenio de la tarjeta (lado propietario: setea el FK). */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private ClienteJpaEntity cliente;

    @Column(name = "numero_mascara", nullable = false, length = 30)
    private String numeroMascara;

    @Column(name = "marca", nullable = false, length = 30)
    private String marca;

    @Column(name = "limite_credito", nullable = false, precision = 15, scale = 2)
    private BigDecimal limiteCredito;

    @Column(name = "saldo_utilizado", nullable = false, precision = 15, scale = 2)
    private BigDecimal saldoUtilizado;

    @Column(name = "fecha_vencimiento")
    private LocalDate fechaVencimiento;

    /** ACTIVA / BLOQUEADA (string en la tabla, enum en el dominio). */
    @Column(name = "estado", nullable = false, length = 20)
    private String estado;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ClienteJpaEntity getCliente() {
        return cliente;
    }

    public void setCliente(ClienteJpaEntity cliente) {
        this.cliente = cliente;
    }

    public String getNumeroMascara() {
        return numeroMascara;
    }

    public void setNumeroMascara(String numeroMascara) {
        this.numeroMascara = numeroMascara;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public BigDecimal getLimiteCredito() {
        return limiteCredito;
    }

    public void setLimiteCredito(BigDecimal limiteCredito) {
        this.limiteCredito = limiteCredito;
    }

    public BigDecimal getSaldoUtilizado() {
        return saldoUtilizado;
    }

    public void setSaldoUtilizado(BigDecimal saldoUtilizado) {
        this.saldoUtilizado = saldoUtilizado;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}