package com.banco.application.dto;

/**
 * DTO de SALIDA para registro y login.
 * Devuelve el token JWT y datos basicos del usuario.
 * REGLA: la contrasena (ni su hash) JAMAS se devuelve en un DTO de salida.
 */
public class LoginResponse {

    private String token;
    private String email;
    private String nombre;
    private String mensaje;

    public LoginResponse() {
    }

    public LoginResponse(String token, String email, String nombre, String mensaje) {
        this.token = token;
        this.email = email;
        this.nombre = nombre;
        this.mensaje = mensaje;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
