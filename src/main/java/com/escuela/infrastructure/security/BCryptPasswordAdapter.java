package com.escuela.infrastructure.security;

import com.escuela.application.port.out.PasswordEncoderPort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Adaptador de seguridad: implementa PasswordEncoderPort usando BCrypt.
 *
 * Regla hexagonal: el hasheo de contrasenas es un DETALLE de infraestructura
 * intercambiable. La capa de aplicacion solo conoce la interfaz.
 */
@Component
public class BCryptPasswordAdapter implements PasswordEncoderPort {

    private final BCryptPasswordEncoder encoder;

    public BCryptPasswordAdapter() {
        this.encoder = new BCryptPasswordEncoder();
    }

    @Override
    public String hashear(String passwordPlano) {
        return encoder.encode(passwordPlano);
    }

    @Override
    public boolean verificar(String passwordPlano, String passwordHash) {
        return encoder.matches(passwordPlano, passwordHash);
    }
}
