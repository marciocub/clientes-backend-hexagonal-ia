package com.escuela.infrastructure.out.db;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data JPA para usuarios (detalle de infraestructura).
 * Solo lo usa UsuarioAdapter, nunca la capa de aplicacion.
 */
public interface UsuarioRepository extends JpaRepository<UsuarioJpaEntity, Long> {

    Optional<UsuarioJpaEntity> findByEmail(String email);

    boolean existsByEmail(String email);
}
