package com.banco.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.banco.domain.exception.ConsumoExcedeDisponibleException;

/**
 * Entidad de dominio: una tarjeta de credito del cliente.
 * Java puro, sin anotaciones de frameworks.
 */
public class TarjetaCredito {

    /** Estados posibles de una tarjeta. */
    public enum Estado {
        ACTIVA,
        BLOQUEADA
    }

    private Long id;
    private String numeroMascara;
    private String marca;
    private BigDecimal limiteCredito;
    private BigDecimal saldoUtilizado;
    private LocalDate fechaVencimiento;
    private Estado estado;

    /** Credito disponible = limite - utilizado. */
    public BigDecimal getCreditoDisponible() {
        BigDecimal limite = limiteCredito != null ? limiteCredito : BigDecimal.ZERO;
        BigDecimal utilizado = saldoUtilizado != null ? saldoUtilizado : BigDecimal.ZERO;
        return limite.subtract(utilizado);
    }

    /**
     * Registra un consumo sobre la tarjeta si hay credito disponible.
     *
     * @param monto consumo a registrar (debe ser positivo)
     * @throws IllegalArgumentException si el monto es nulo o negativo
     * @throws ConsumoExcedeDisponibleException si no alcanza el credito disponible
     */
    public void registrarConsumo(BigDecimal monto) {
        if (monto == null || monto.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El consumo debe ser un monto positivo");
        }
        if (monto.compareTo(getCreditoDisponible()) > 0) {
            throw new ConsumoExcedeDisponibleException(
                    "El consumo de " + monto + " excede el credito disponible ("
                            + getCreditoDisponible() + ") de la tarjeta " + numeroMascara);
        }
        saldoUtilizado = (saldoUtilizado == null ? BigDecimal.ZERO : saldoUtilizado).add(monto);
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

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }
}