package com.escuela.infrastructure.security;

import com.escuela.application.port.out.TokenProviderPort;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Adaptador de seguridad: implementa TokenProviderPort usando JJWT.
 *
 * Regla hexagonal: la generacion de tokens es un DETALLE de infraestructura
 * intercambiable. La capa de aplicacion solo conoce la interfaz.
 * - Firma: HS256 (clave HMAC desde app.jwt.secret).
 * - Expiracion: app.jwt.expiration (milisegundos).
 * - Subject: email del usuario.
 */
@Component
public class JwtTokenAdapter implements TokenProviderPort {

    private final SecretKey clave;
    private final long expiracionMs;

    public JwtTokenAdapter(@Value("${app.jwt.secret}") String secreto,
                           @Value("${app.jwt.expiration}") long expiracionMs) {
        this.clave = Keys.hmacShaKeyFor(secreto.getBytes(StandardCharsets.UTF_8));
        this.expiracionMs = expiracionMs;
    }

    @Override
    public String generarToken(String email) {
        Date ahora = new Date();
        return Jwts.builder()
                .subject(email)
                .issuedAt(ahora)
                .expiration(new Date(ahora.getTime() + expiracionMs))
                .signWith(clave, Jwts.SIG.HS256)
                .compact();
    }

    @Override
    public boolean validarToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(clave)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            return false; // firma invalida, token expirado o malformado
        }
    }

    @Override
    public String extraerEmail(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(clave)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }
}
