package com.banco.application.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO de un item de factura: entrada para CrearFacturaDTO y salida
 * dentro de FacturaDtoResponse (el subtotal lo calcula el dominio).
 */
public class ItemFacturaDTO {

    @NotBlank(message = "La descripcion es obligatoria")
    @Size(max = 200, message = "La descripcion no puede superar los 200 caracteres")
    private String descripcion;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser mayor a 0")
    private Integer cantidad;

    @NotNull(message = "El precio unitario es obligatorio")
    @DecimalMin(value = "0.00", message = "El precio unitario no puede ser negativo")
    @Digits(integer = 13, fraction = 2, message = "El precio admite 13 digitos enteros y 2 decimales")
    private BigDecimal precioUnitario;

    /** Solo SALIDA: subtotal = cantidad x precioUnitario (calculado por el dominio). */
    private BigDecimal subtotal;

    public ItemFacturaDTO() {
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
}