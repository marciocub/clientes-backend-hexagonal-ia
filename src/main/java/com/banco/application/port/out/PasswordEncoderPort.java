package com.banco.application.port.out;

/**
 * Puerto de SALIDA: contrato para hashear y verificar contrasenas.
 * La aplicacion define QUE necesita, no COMO se hashea.
 * Lo implementa el adaptador de seguridad (infrastructure/security/BCryptPasswordAdapter).
 */
public interface PasswordEncoderPort {

    /** Hashea una contrasena en texto plano (nunca se guarda en claro). */
    String hashear(String passwordPlano);

    /** Verifica si una contrasena en claro coincide con el hash almacenado. */
    boolean verificar(String passwordPlano, String passwordHash);
}
