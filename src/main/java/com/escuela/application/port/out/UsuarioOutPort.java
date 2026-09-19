package com.escuela.application.port.out;

import com.escuela.domain.model.Usuario;

import java.util.Optional;

/**
 * Puerto de SALIDA: contrato de persistencia de usuarios.
 * Lo implementa el adaptador de base de datos (infrastructure/out/db/UsuarioAdapter).
 */
public interface UsuarioOutPort {

    /** Guarda un usuario nuevo (la contrasena ya llega hasheada). */
    Usuario guardar(Usuario usuario);

    /** Busca un usuario por email (unico). */
    Optional<Usuario> buscarPorEmail(String email);

    /** Indica si ya existe un usuario con ese email. */
    boolean existePorEmail(String email);
}
