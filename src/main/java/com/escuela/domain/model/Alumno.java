package com.escuela.domain.model;

import java.time.LocalDateTime;

/**
 * Modelo de negocio puro de un Alumno.
 *
 * Regla hexagonal: SIN anotaciones JPA (@Entity, @Table, @Column),
 * SIN imports de Spring ni Lombok. Solo negocio.
 */
public class Alumno {

    /** Estados posibles de un alumno. */
    public enum Estado {
        ACTIVO,
        INACTIVO
    }

    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private Estado estado;
    private LocalDateTime fechaInscripcion;

    public Alumno() {
        // Defaults de negocio: estado ACTIVO y fecha de inscripcion actual
        this.estado = Estado.ACTIVO;
        this.fechaInscripcion = LocalDateTime.now();
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

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaInscripcion() {
        return fechaInscripcion;
    }

    public void setFechaInscripcion(LocalDateTime fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }
}
