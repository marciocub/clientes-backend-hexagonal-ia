package com.banco.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.banco.domain.exception.FacturaYaPagadaException;

/**
 * Entidad de dominio: una factura emitida a un cliente, con sus items.
 * Java puro, sin anotaciones de frameworks.
 */
public class Factura {

    /** Estados posibles de una factura. */
    public enum Estado {
        PENDIENTE,
        PAGADA
    }

    private Long id;
    private String numeroFactura;
    private LocalDateTime fechaEmision;
    private BigDecimal montoTotal;
    private Estado estado;
    private List<ItemFactura> items = new ArrayList<>();

    public Factura() {
        // Defaults de negocio: se emite ahora y queda pendiente de pago
        this.fechaEmision = LocalDateTime.now();
        this.estado = Estado.PENDIENTE;
    }

    /** Calcula el monto total sumando el subtotal de cada item. */
    public BigDecimal calcularTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (ItemFactura item : items) {
            if (item.getSubtotal() != null) {
                total = total.add(item.getSubtotal());
            }
        }
        this.montoTotal = total;
        return total;
    }

    /**
     * Paga la factura (PENDIENTE -&gt; PAGADA).
     *
     * @throws FacturaYaPagadaException si la factura ya esta pagada
     */
    public void pagar() {
        if (this.estado == Estado.PAGADA) {
            throw new FacturaYaPagadaException("La factura " + numeroFactura + " ya esta pagada");
        }
        this.estado = Estado.PAGADA;
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

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public List<ItemFactura> getItems() {
        return items;
    }

    public void setItems(List<ItemFactura> items) {
        this.items = items != null ? items : new ArrayList<>();
    }
}