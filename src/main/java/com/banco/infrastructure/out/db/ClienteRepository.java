package com.banco.infrastructure.out.db;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA para el agregado de clientes (detalle de infraestructura).
 * Solo lo usa ClienteAdapter, nunca la capa de aplicacion.
 * Las tarjetas/facturas/items se leen y guardan EN CASCADA desde el cliente:
 * no hace falta un repository por cada entidad del agregado.
 */
public interface ClienteRepository extends JpaRepository<ClienteJpaEntity, Long> {
}