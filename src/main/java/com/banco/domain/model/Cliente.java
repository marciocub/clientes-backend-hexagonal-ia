package com.banco.domain.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.banco.domain.exception.LimiteTarjetasExcedidoException;

/**
 * AGGREGATE ROOT de negocio: un Cliente con sus Tarjetas de Credito y Facturas.
 *
 * Regla hexagonal: SIN anotaciones JPA (@Entity, @Table, @Column),
 * SIN imports de Spring ni Lombok. Solo negocio.
 *
 * Reglas del agregado (viven AQUI, no en el service):
 * - solicitarTarjeta(): el limite total acumulado de las tarjetas no puede
 *   superar el tope de negocio (TOPE_LIMITE_TOTAL_CREDITO = $2.000.000).
 * - emitirFactura(): el monto total se calcula sumando los items.
 */
public class Cliente {

    /** Tope de negocio: suma de los limites de todas las tarjetas del cliente. */
    public static final BigDecimal TOPE_LIMITE_TOTAL_CREDITO = new BigDecimal("2000000");

    private Long id;
    private String nombre;
    private String cuit;
    private List<TarjetaCredito> tarjetas = new ArrayList<>();
    private List<Factura> facturas = new ArrayList<>();

    public Cliente() {
    }

    /** Constructor completo: lo usan el service y el mapper JPA. */
    public Cliente(Long id, String nombre, String cuit) {
        this.id = id;
        this.nombre = nombre;
        this.cuit = cuit;
    }

    // ------------------------------------------------------------------
    // Metodos de negocio (reglas del agregado)
    // ------------------------------------------------------------------

    /**
     * Solicita una tarjeta de credito: valida que el limite total acumulado
     * de sus tarjetas no supere el tope maximo antes de agregarla.
     *
     * @param marca             marca de la tarjeta (ej: VISA)
     * @param limiteSolicitado  limite de credito solicitado
     * @param ultimoCuatro      ultimos 4 digitos (se guardan enmascarados)
     * @return la tarjeta creada y agregada al cliente
     * @throws LimiteTarjetasExcedidoException si se supera el tope acumulado
     */
    public TarjetaCredito solicitarTarjeta(String marca, BigDecimal limiteSolicitado, String ultimoCuatro) {
        BigDecimal acumulado = BigDecimal.ZERO;
        for (TarjetaCredito tarjeta : tarjetas) {
            if (tarjeta.getLimiteCredito() != null) {
                acumulado = acumulado.add(tarjeta.getLimiteCredito());
            }
        }
        BigDecimal nuevoTotal = acumulado.add(limiteSolicitado != null ? limiteSolicitado : BigDecimal.ZERO);
        if (nuevoTotal.compareTo(TOPE_LIMITE_TOTAL_CREDITO) > 0) {
            throw new LimiteTarjetasExcedidoException(
                    "No se puede asignar el limite " + limiteSolicitado
                            + ": el total acumulado de tarjetas (" + acumulado
                            + ") superaria el tope de " + TOPE_LIMITE_TOTAL_CREDITO);
        }
        TarjetaCredito tarjeta = new TarjetaCredito();
        tarjeta.setMarca(marca);
        tarjeta.setLimiteCredito(limiteSolicitado);
        tarjeta.setNumeroMascara("****-****-****-" + ultimoCuatro);
        tarjeta.setEstado(TarjetaCredito.Estado.ACTIVA);
        tarjetas.add(tarjeta);
        return tarjeta;
    }

    /**
     * Emite una factura: calcula el monto total sumando los items y la agrega
     * a la lista de facturas del cliente.
     *
     * @param numeroFactura numero identificatorio de la factura
     * @param items         items de la factura (el subtotal se recalcula por item)
     * @return la factura creada y agregada al cliente
     */
    public Factura emitirFactura(String numeroFactura, List<ItemFactura> items) {
        Factura factura = new Factura();
        factura.setNumeroFactura(numeroFactura);
        factura.setEstado(Factura.Estado.PENDIENTE);
        if (items != null) {
            for (ItemFactura item : items) {
                item.calcularSubtotal();
                factura.getItems().add(item);
            }
        }
        factura.calcularTotal();
        facturas.add(factura);
        return factura;
    }

    // ------------------------------------------------------------------
    // Getters / Setters
    // ------------------------------------------------------------------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCuit() {
        return cuit;
    }

    public void setCuit(String cuit) {
        this.cuit = cuit;
    }

    public List<TarjetaCredito> getTarjetas() {
        return tarjetas;
    }

    public void setTarjetas(List<TarjetaCredito> tarjetas) {
        this.tarjetas = tarjetas != null ? tarjetas : new ArrayList<>();
    }

    public List<Factura> getFacturas() {
        return facturas;
    }

    public void setFacturas(List<Factura> facturas) {
        this.facturas = facturas != null ? facturas : new ArrayList<>();
    }
}