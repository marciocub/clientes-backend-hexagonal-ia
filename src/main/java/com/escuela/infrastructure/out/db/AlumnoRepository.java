package com.escuela.infrastructure.out.db;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA para alumnos (detalle de infraestructura).
 * Solo lo usa AlumnoAdapter, nunca la capa de aplicacion.
 */
public interface AlumnoRepository extends JpaRepository<AlumnoJpaEntity, Long> {

    Optional<AlumnoJpaEntity> findByEmail(String email);

    List<AlumnoJpaEntity> findByEstado(String estado);
}
