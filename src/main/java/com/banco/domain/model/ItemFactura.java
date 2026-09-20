package com.banco.domain.model;

import java.math.BigDecimal;

/**
 * Value Object / Entidad de dominio: un item de factura.
 * Java puro, sin anotaciones de frameworks.
 */
public class ItemFactura {

    private Long id;
    private String descripcion;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;

    /** Calcula y setea el subtotal = cantidad x precioUnitario. */
    public BigDecimal calcularSubtotal() {
        BigDecimal cant = cantidad != null ? BigDecimal.valueOf(cantidad) : BigDecimal.ZERO;
        BigDecimal precio = precioUnitario != null ? precioUnitario : BigDecimal.ZERO;
        this.subtotal = cant.multiply(precio);
        return this.subtotal;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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