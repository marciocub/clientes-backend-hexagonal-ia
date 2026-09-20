package com.escuela.infrastructure.out.db;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA para clientes (detalle de infraestructura).
 * Solo lo usa ClienteAdapter, nunca la capa de aplicacion.
 */
public interface CuentaRepository extends JpaRepository<ClienteJpaEntity, Long> {

    Optional<ClienteJpaEntity> findByEmail(String email);

    List<ClienteJpaEntity> findByEstado(String estado);
}
