package com.banco.application.port.out;

import java.util.List;
import java.util.Optional;

import com.banco.domain.model.Cliente;

/**
 * Puerto de SALIDA: contrato de persistencia del AGREGADO Cliente
 * (se guardan cliente, tarjetas, facturas e items en cascada).
 * Se expresa en terminos del MODELO DE DOMINIO (Cliente), nunca de JPA.
 * Lo implementa el adaptador de base de datos (infrastructure/out/db/ClienteAdapter).
 */
public interface ClienteOutPort {

    /** Guarda el agregado completo (alta o actualizacion en cascada). */
    Cliente guardar(Cliente cliente);

    /** Busca el agregado completo por id. */
    Optional<Cliente> buscarPorId(Long id);

    /** Lista todos los clientes con su agregado completo. */
    List<Cliente> listarTodos();

    /** Elimina el agregado completo (orphanRemoval borra hijos). */
    void eliminar(Cliente cliente);
}