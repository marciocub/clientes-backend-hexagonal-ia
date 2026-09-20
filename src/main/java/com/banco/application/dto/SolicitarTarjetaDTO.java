package com.banco.application.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO de ENTRADA para solicitar una tarjeta de credito a un cliente.
 * La regla del tope acumulado vive en el dominio (Cliente.solicitarTarjeta).
 */
public class SolicitarTarjetaDTO {

    @NotBlank(message = "La marca es obligatoria")
    @Size(max = 30, message = "La marca no puede superar los 30 caracteres")
    private String marca;

    @NotNull(message = "El limite solicitado es obligatorio")
    @DecimalMin(value = "0.01", message = "El limite solicitado debe ser mayor a 0")
    @Digits(integer = 13, fraction = 2, message = "El limite admite 13 digitos enteros y 2 decimales")
    private BigDecimal limiteSolicitado;

    @NotBlank(message = "Los ultimos 4 digitos son obligatorios")
    @Pattern(regexp = "\\d{4}", message = "Los ultimos 4 digitos deben ser numericos")
    private String ultimoCuatro;

    public SolicitarTarjetaDTO() {
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public BigDecimal getLimiteSolicitado() {
        return limiteSolicitado;
    }

    public void setLimiteSolicitado(BigDecimal limiteSolicitado) {
        this.limiteSolicitado = limiteSolicitado;
    }

    public String getUltimoCuatro() {
        return ultimoCuatro;
    }

    public void setUltimoCuatro(String ultimoCuatro) {
        this.ultimoCuatro = ultimoCuatro;
    }
}