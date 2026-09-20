package com.banco.infrastructure.mapper;

import com.banco.domain.model.Rol;
import com.banco.domain.model.Usuario;
import com.banco.infrastructure.out.db.UsuarioJpaEntity;

/**
 * Mapper JPA &lt;-&gt; Dominio para usuarios.
 * IMPORTANTE: el hash de la contrasena solo vive en la entidad JPA;
 * jamas sale hacia DTOs de respuesta.
 */
public final class UsuarioJpaMapper {

    private UsuarioJpaMapper() {
    }

    /** Dominio -&gt; JPA (para guardar). */
    public static UsuarioJpaEntity aEntity(Usuario usuario) {
        UsuarioJpaEntity entity = new UsuarioJpaEntity();
        entity.setId(usuario.getId());
        entity.setNombre(usuario.getNombre());
        entity.setEmail(usuario.getEmail());
        entity.setPasswordHash(usuario.getPasswordHash());
        entity.setRol(usuario.getRol() != null ? usuario.getRol() : Rol.USER);
        entity.setActivo(usuario.isActivo());
        entity.setFechaCreacion(usuario.getFechaCreacion());
        return entity;
    }

    /** JPA -&gt; Dominio (para leer). */
    public static Usuario aDominio(UsuarioJpaEntity entity) {
        Usuario usuario = new Usuario();
        usuario.setId(entity.getId());
        usuario.setNombre(entity.getNombre());
        usuario.setEmail(entity.getEmail());
        usuario.setPasswordHash(entity.getPasswordHash());
        usuario.setRol(entity.getRol() != null ? entity.getRol() : Rol.USER);
        usuario.setActivo(entity.isActivo());
        usuario.setFechaCreacion(entity.getFechaCreacion());
        return usuario;
    }
}
