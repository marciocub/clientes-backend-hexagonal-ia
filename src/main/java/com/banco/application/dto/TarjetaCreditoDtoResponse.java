package com.banco.application.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO de SALIDA de una tarjeta de credito del cliente.
 */
public class TarjetaCreditoDtoResponse {

    private Long id;
    private String numeroMascara;
    private String marca;
    private BigDecimal limiteCredito;
    private BigDecimal saldoUtilizado;
    private BigDecimal creditoDisponible;
    private LocalDate fechaVencimiento;
    private String estado;

    public TarjetaCreditoDtoResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public BigDecimal getCreditoDisponible() {
        return creditoDisponible;
    }

    public void setCreditoDisponible(BigDecimal creditoDisponible) {
        this.creditoDisponible = creditoDisponible;
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