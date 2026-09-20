package com.banco.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO de ENTRADA para crear / actualizar un cliente (Aggregate Root).
 * Atraviesa el puerto de entrada (ClienteUseCase): nunca viaja la entidad de dominio.
 * Las tarjetas y facturas NO van aqui: se agregan por endpoints propios.
 */
public class ClienteDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    private String nombre;

    @NotBlank(message = "El CUIT es obligatorio")
    @Pattern(regexp = "\\d{2}-?\\d{8}-?\\d{1}",
            message = "El CUIT debe tener el formato 20-12345678-9 (o 20123456789)")
    private String cuit;

    public ClienteDTO() {
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
}