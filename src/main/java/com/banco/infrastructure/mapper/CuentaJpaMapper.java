package com.banco.infrastructure.mapper;

import com.banco.domain.Enum.EstadoCuenta;
import com.banco.domain.Enum.Moneda;
import com.banco.domain.model.Cuenta;
import com.banco.infrastructure.out.db.CuentaJpaEntity;

/**
 * Mapper JPA &lt;-&gt; Dominio para cuentas.
 * La entidad de dominio nunca se persiste ni se expone: se convierte a JPA
 * para persistir y desde JPA hacia dominio al leer.
 */
public final class CuentaJpaMapper {

    private CuentaJpaMapper() {
    }

    /** Dominio -&gt; JPA (para guardar). */
    public static CuentaJpaEntity aEntity(Cuenta cuenta) {
        CuentaJpaEntity entity = new CuentaJpaEntity();
        entity.setId(cuenta.getId());
        entity.setNumeroCuenta(cuenta.getNumeroCuenta());
        entity.setClienteId(cuenta.getClienteId());
        entity.setSaldo(cuenta.getSaldo());
        entity.setMoneda(cuenta.getMoneda() != null ? cuenta.getMoneda().name() : Moneda.PESOS.name());
        entity.setEstado(cuenta.getEstado() != null ? cuenta.getEstado().name() : EstadoCuenta.INACTIVO.name());
        return entity;
    }

    /** JPA -&gt; Dominio (para leer). Usa el constructor completo porque el id del dominio es final. */
    public static Cuenta aDominio(CuentaJpaEntity entity) {
        return new Cuenta(
                entity.getId(),
                entity.getNumeroCuenta(),
                entity.getClienteId(),
                entity.getSaldo(),
                entity.getMoneda() != null ? Moneda.valueOf(entity.getMoneda()) : Moneda.PESOS,
                entity.getEstado() != null ? EstadoCuenta.valueOf(entity.getEstado()) : EstadoCuenta.INACTIVO);
    }
}