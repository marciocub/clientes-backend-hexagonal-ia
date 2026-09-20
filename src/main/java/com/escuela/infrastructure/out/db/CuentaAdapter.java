package com.escuela.infrastructure.out.db;

import com.escuela.application.port.out.CuentaOutPort;
import com.escuela.domain.Enum.EstadoCuenta;
import com.escuela.domain.model.Cuenta;
import com.escuela.infrastructure.mapper.CuentaJpaMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Adaptador de salida a base de datos: implementa CuentaOutPort.
 * Delega en el CuentaRepository (Spring Data JPA) y usa el mapper
 * para convertir entre dominio y JPA.
 */
@Repository
public class CuentaAdapter implements CuentaOutPort {

    private final CuentaRepository cuentaRepository;

    public CuentaAdapter(CuentaRepository cuentaRepository) {
        this.cuentaRepository = cuentaRepository;
    }

    @Override
    public Cuenta guardar(Cuenta cuenta) {
        CuentaJpaEntity guardada = cuentaRepository.save(CuentaJpaMapper.aEntity(cuenta));
        return CuentaJpaMapper.aDominio(guardada);
    }

    @Override
    public Optional<Cuenta> buscarPorId(Long id) {
        return cuentaRepository.findById(id).map(CuentaJpaMapper::aDominio);
    }

    @Override
    public Optional<Cuenta> buscarPorNumeroCuenta(String numeroCuenta) {
        return cuentaRepository.findByNumeroCuenta(numeroCuenta).map(CuentaJpaMapper::aDominio);
    }

    @Override
    public List<Cuenta> listarTodos() {
        return cuentaRepository.findAll().stream()
                .map(CuentaJpaMapper::aDominio)
                .toList();
    }

    @Override
    public List<Cuenta> listarPorEstado(EstadoCuenta estado) {
        return cuentaRepository.findByEstado(estado.name()).stream()
                .map(CuentaJpaMapper::aDominio)
                .toList();
    }

    @Override
    public void eliminar(Cuenta cuenta) {
        cuentaRepository.deleteById(cuenta.getId());
    }
}