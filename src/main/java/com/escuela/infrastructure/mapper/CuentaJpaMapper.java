package com.escuela.infrastructure.mapper;

import com.escuela.domain.model.Cliente;
import com.escuela.infrastructure.out.db.ClienteJpaEntity;

/**
 * Mapper JPA &lt;-&gt; Dominio para clientes.
 * La entidad de dominio nunca se persiste ni se expone: se convierte a JPA
 * para persistir y desde JPA hacia dominio al leer.
 */
public final class CuentaJpaMapper {

    private CuentaJpaMapper() {
    }

    /** Dominio -&gt; JPA (para guardar). */
    public static ClienteJpaEntity aEntity(Cliente cliente) {
        ClienteJpaEntity entity = new ClienteJpaEntity();
        entity.setId(cliente.getId());
        entity.setNombre(cliente.getNombre());
        entity.setApellido(cliente.getApellido());
        entity.setEmail(cliente.getEmail());
        entity.setTelefono(cliente.getTelefono());
        entity.setEstado(cliente.getEstado() != null ? cliente.getEstado().name() : Cliente.Estado.ACTIVO.name());
        entity.setFechaInscripcion(cliente.getFechaInscripcion());
        return entity;
    }

    /** JPA -&gt; Dominio (para leer). */
    public static Cliente aDominio(ClienteJpaEntity entity) {
        Cliente cliente = new Cliente();
        cliente.setId(entity.getId());
        cliente.setNombre(entity.getNombre());
        cliente.setApellido(entity.getApellido());
        cliente.setEmail(entity.getEmail());
        cliente.setTelefono(entity.getTelefono());
        cliente.setEstado(entity.getEstado() != null
                ? Cliente.Estado.valueOf(entity.getEstado())
                : Cliente.Estado.ACTIVO);
        cliente.setFechaInscripcion(entity.getFechaInscripcion());
        return cliente;
    }
}
