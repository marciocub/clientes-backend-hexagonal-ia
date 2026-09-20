package com.banco.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO de SALIDA de una factura con sus items.
 */
public class FacturaDtoResponse {

    private Long id;
    private String numeroFactura;
    private LocalDateTime fechaEmision;
    private BigDecimal montoTotal;
    private String estado;
    private List<ItemFacturaDTO> items = new ArrayList<>();

    public FacturaDtoResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public LocalDateTime getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDateTime fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public BigDecimal getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(BigDecimal montoTotal) {
        this.montoTotal = montoTotal;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<ItemFacturaDTO> getItems() {
        return items;
    }

    public void setItems(List<ItemFacturaDTO> items) {
        this.items = items;
    }
}