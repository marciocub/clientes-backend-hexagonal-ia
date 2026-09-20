package com.banco.application.port.out;

/**
 * Puerto de SALIDA: contrato para generar y validar tokens JWT.
 * La aplicacion define QUE necesita, no COMO se firma el token.
 * Lo implementa el adaptador de seguridad (infrastructure/security/JwtTokenAdapter).
 */
public interface TokenProviderPort {

    /** Genera un token JWT firmado (subject = email del usuario). */
    String generarToken(String email);

    /** Valida firma y expiracion del token. */
    boolean validarToken(String token);

    /** Extrae el subject (email) de un token valido. */
    String extraerEmail(String token);
}
