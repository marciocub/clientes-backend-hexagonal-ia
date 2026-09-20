package com.escuela.application.port.out;



import java.util.List;
import java.util.Optional;

import com.escuela.domain.Enum.EstadoCuenta;
import com.escuela.domain.model.Cuenta;

/**
 * Puerto de SALIDA: contrato de persistencia de Cuentas.
 * Se expresa en terminos del MODELO DE DOMINIO (Cuenta), nunca de JPA.
 * Lo implementa el adaptador de base de datos (infrastructure/out/db/CuentaAdapter).
 */
public interface CuentaOutPort {

    /** Guarda un Cuenta nuevo o actualiza uno existente. */
    Cuenta guardar(Cuenta cuenta);

    /** Busca un Cuenta por id. */
    Optional<Cuenta> buscarPorId(Long id);

    /** Busca un Cuenta por email (unico). */
    Optional<Cuenta> buscarPorEmail(String email);

    /** Lista todos los Cuentas. */
    List<Cuenta> listarTodos();

    /** Lista Cuentas por estado. */
    List<Cuenta> listarPorEstado(EstadoCuenta estado);

    /** Elimina un Cuenta. */
    void eliminar(Cuenta cuenta);
}
