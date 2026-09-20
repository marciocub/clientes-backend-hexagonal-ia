package com.banco.domain.model;

import java.time.LocalDateTime;

/**
 * Modelo de negocio puro de un Usuario (para autenticacion).
 *
 * Regla hexagonal: SIN anotaciones JPA, SIN imports de Spring ni Lombok.
 * La contrasena viaja SIEMPRE como hash (passwordHash), jamas en texto plano.
 */
public class Usuario {

    private Long id;
    private String nombre;
    private String email;
    private String passwordHash;
    private Rol rol;
    private boolean activo;
    private LocalDateTime fechaCreacion;

    public Usuario() {
        // Defaults de negocio: rol USER y usuario activo
        this.rol = Rol.USER;
        this.activo = true;
        this.fechaCreacion = LocalDateTime.now();
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
