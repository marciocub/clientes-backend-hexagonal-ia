package com.escuela.application.port.in;

import com.escuela.application.dto.LoginRequest;
import com.escuela.application.dto.LoginResponse;
import com.escuela.application.dto.RegistroRequest;

/**
 * Puerto de ENTRADA: casos de uso de autenticacion (registrar y autenticar).
 * El adaptador REST (UsuarioController) consume esta interfaz.
 */
public interface AuthUseCase {

    /** Registra un usuario: valida email unico, hashea la contrasena y devuelve token JWT. */
    LoginResponse registrar(RegistroRequest request);

    /** Autentica un usuario: verifica contrasena con BCrypt y devuelve token JWT. */
    LoginResponse login(LoginRequest request);
}
