package com.banco.application.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO de SALIDA con el AGREGADO completo: cliente + sus tarjetas + sus facturas.
 * Nunca expone la entidad JPA ni las entidades de dominio.
 */
public class ClienteDtoResponse {

    private Long id;
    private String nombre;
    private String cuit;
    private List<TarjetaCreditoDtoResponse> tarjetas = new ArrayList<>();
    private List<FacturaDtoResponse> facturas = new ArrayList<>();

    public ClienteDtoResponse() {
    }

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

    public List<TarjetaCreditoDtoResponse> getTarjetas() {
        return tarjetas;
    }

    public void setTarjetas(List<TarjetaCreditoDtoResponse> tarjetas) {
        this.tarjetas = tarjetas;
    }

    public List<FacturaDtoResponse> getFacturas() {
        return facturas;
    }

    public void setFacturas(List<FacturaDtoResponse> facturas) {
        this.facturas = facturas;
    }
}