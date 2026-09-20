package com.escuela.application.dto;

import java.math.BigDecimal;

import com.escuela.domain.Enum.EstadoCuenta;
import com.escuela.domain.Enum.Moneda;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO de ENTRADA para crear / actualizar una cuenta.
 * Atraviesa el puerto de entrada (CuentaUseCase): nunca viaja la entidad de dominio.
 * El id NO viaja en la entrada: lo genera la base de datos.
 */
public class CuentaDTO {

    @NotBlank(message = "El numero de cuenta es obligatorio")
    @Size(max = 30, message = "El numero de cuenta no puede superar los 30 caracteres")
    private String numeroCuenta;

    @NotNull(message = "El clienteId es obligatorio")
    private Long clienteId;

    @NotNull(message = "El saldo es obligatorio")
    @Digits(integer = 13, fraction = 2, message = "El saldo admite 13 digitos enteros y 2 decimales")
    @DecimalMin(value = "0.00", message = "El saldo no puede ser negativo")
    private BigDecimal saldo;

    @NotNull(message = "La moneda es obligatoria")
    private Moneda moneda;

    /** Opcional: ACTIVO / INACTIVO. Si no llega se usa INACTIVO por defecto. */
    private EstadoCuenta estado;

    public CuentaDTO() {
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