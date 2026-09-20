package com.escuela.infrastructure.out.db;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA para cuentas (detalle de infraestructura).
 * Solo lo usa CuentaAdapter, nunca la capa de aplicacion.
 */
public interface CuentaRepository extends JpaRepository<CuentaJpaEntity, Long> {

    Optional<CuentaJpaEntity> findByNumeroCuenta(String numeroCuenta);

    List<CuentaJpaEntity> findByEstado(String estado);
}