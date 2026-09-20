package com.escuela.application.port.out;

import com.escuela.domain.model.Cliente;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de SALIDA: contrato de persistencia de clientes.
 * Se expresa en terminos del MODELO DE DOMINIO (Cliente), nunca de JPA.
 * Lo implementa el adaptador de base de datos (infrastructure/out/db/ClienteAdapter).
 */
public interface ClienteOutPort {

    /** Guarda un cliente nuevo o actualiza uno existente. */
    Cliente guardar(Cliente cliente);

    /** Busca un cliente por id. */
    Optional<Cliente> buscarPorId(Long id);

    /** Busca un cliente por email (unico). */
    Optional<Cliente> buscarPorEmail(String email);

    /** Lista todos los clientes. */
    List<Cliente> listarTodos();

    /** Lista clientes por estado. */
    List<Cliente> listarPorEstado(Cliente.Estado estado);

    /** Elimina un cliente. */
    void eliminar(Cliente cliente);
}
