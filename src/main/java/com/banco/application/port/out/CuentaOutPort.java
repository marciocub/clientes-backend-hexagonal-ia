package com.banco.application.port.out;

import java.util.List;
import java.util.Optional;

import com.banco.domain.Enum.EstadoCuenta;
import com.banco.domain.model.Cuenta;

/**
 * Puerto de SALIDA: contrato de persistencia de cuentas.
 * Se expresa en terminos del MODELO DE DOMINIO (Cuenta), nunca de JPA.
 * Lo implementa el adaptador de base de datos (infrastructure/out/db/CuentaAdapter).
 */
public interface CuentaOutPort {

    /** Guarda una cuenta nueva o actualiza una existente. */
    Cuenta guardar(Cuenta cuenta);

    /** Busca una cuenta por id. */
    Optional<Cuenta> buscarPorId(Long id);

    /** Busca una cuenta por numero de cuenta (unico). */
    Optional<Cuenta> buscarPorNumeroCuenta(String numeroCuenta);

    /** Lista todas las cuentas. */
    List<Cuenta> listarTodos();

    /** Lista cuentas por estado. */
    List<Cuenta> listarPorEstado(EstadoCuenta estado);

    /** Elimina una cuenta. */
    void eliminar(Cuenta cuenta);
}