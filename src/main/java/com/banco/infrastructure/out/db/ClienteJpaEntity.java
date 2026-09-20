package com.banco.infrastructure.out.db;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * Unica clase de cliente con anotaciones JPA (adaptador de salida a BD).
 * El AGREGADO de dominio (domain/model/Cliente) permanece puro.
 * Tabla: clientes. Raiz del agregado: el ciclo de vida de tarjetas y
 * facturas depende del cliente (CascadeType.ALL + orphanRemoval).
 */
@Entity
@Table(name = "clientes")
public class ClienteJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "cuit", nullable = false, length = 13)
    private String cuit;

    /** Tarjetas de credito del cliente (cascade ALL + orphanRemoval). */
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TarjetaCreditoJpaEntity> tarjetas = new ArrayList<>();

    /** Facturas del cliente (cascade ALL + orphanRemoval). */
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FacturaJpaEntity> facturas = new ArrayList<>();

    public ClienteJpaEntity() {
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

    public List<TarjetaCreditoJpaEntity> getTarjetas() {
        return tarjetas;
    }

    public void setTarjetas(List<TarjetaCreditoJpaEntity> tarjetas) {
        this.tarjetas = tarjetas;
    }

    public List<FacturaJpaEntity> getFacturas() {
        return facturas;
    }

    public void setFacturas(List<FacturaJpaEntity> facturas) {
        this.facturas = facturas;
    }
}