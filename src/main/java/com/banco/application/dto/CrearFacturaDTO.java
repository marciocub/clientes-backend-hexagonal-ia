package com.banco.application.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

/**
 * DTO de ENTRADA para emitir una factura a un cliente (con sus items).
 * El monto total lo calcula el dominio (Factura.calcularTotal), no viaja aqui.
 */
public class CrearFacturaDTO {

    @NotBlank(message = "El numero de factura es obligatorio")
    @Size(max = 30, message = "El numero de factura no puede superar los 30 caracteres")
    private String numeroFactura;

    @NotEmpty(message = "La factura debe tener al menos un item")
    @Valid
    private List<ItemFacturaDTO> items;

    public CrearFacturaDTO() {
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public List<ItemFacturaDTO> getItems() {
        return items;
    }

    public void setItems(List<ItemFacturaDTO> items) {
        this.items = items;
    }
}