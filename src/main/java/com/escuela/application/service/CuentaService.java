package com.escuela.application.service;

import com.escuela.application.dto.CuentaDTO;
import com.escuela.application.dto.CuentaDtoResponse;
import com.escuela.application.port.in.CuentaUseCase;
import com.escuela.application.port.out.CuentaOutPort;
import com.escuela.domain.Enum.EstadoCuenta;
import com.escuela.domain.exception.CuentaNoEncontradoException;
import com.escuela.domain.exception.NumeroCuentaDuplicadoException;
import com.escuela.domain.model.Cuenta;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio de aplicacion: implementa el puerto de entrada CuentaUseCase.
 *
 * Regla hexagonal: SOLO conoce el dominio y sus propios puertos.
 * Inyecta la interfaz CuentaOutPort (puerto de salida), nunca clases
 * concretas de infraestructura (JPA, web, seguridad).
 */
@Service
public class CuentaService implements CuentaUseCase {

    private final CuentaOutPort cuentaOutPort;

    public CuentaService(CuentaOutPort cuentaOutPort) {
        this.cuentaOutPort = cuentaOutPort;
    }

    @Override
    public CuentaDtoResponse crear(CuentaDTO dto) {
        validarNumeroCuentaDisponible(dto.getNumeroCuenta(), null);
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(dto.getNumeroCuenta().trim());
        cuenta.setClienteId(dto.getClienteId());
        cuenta.setSaldo(dto.getSaldo());
        cuenta.setMoneda(dto.getMoneda());
        cuenta.setEstado(resolverEstado(dto.getEstado()));
        Cuenta guardada = cuentaOutPort.guardar(cuenta);
        return aResponse(guardada);
    }

    @Override
    public CuentaDtoResponse obtenerPorId(Long id) {
        return aResponse(buscarCuenta(id));
    }

    @Override
    public List<CuentaDtoResponse> listarTodos() {
        return cuentaOutPort.listarTodos().stream()
                .map(this::aResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CuentaDtoResponse> listarPorEstado(String estado) {
        return cuentaOutPort.listarPorEstado(parseEstado(estado)).stream()
                .map(this::aResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CuentaDtoResponse actualizar(Long id, CuentaDTO dto) {
        Cuenta existente = buscarCuenta(id);
        validarNumeroCuentaDisponible(dto.getNumeroCuenta(), id);
        existente.setNumeroCuenta(dto.getNumeroCuenta().trim());
        existente.setClienteId(dto.getClienteId());
        existente.setSaldo(dto.getSaldo());
        existente.setMoneda(dto.getMoneda());
        existente.setEstado(resolverEstado(dto.getEstado()));
        return aResponse(cuentaOutPort.guardar(existente));
    }

    @Override
    public void eliminar(Long id) {
        Cuenta existente = buscarCuenta(id);
        cuentaOutPort.eliminar(existente);
    }

    // ------------------------------------------------------------------
    // Reglas de negocio privadas
    // ------------------------------------------------------------------

    private Cuenta buscarCuenta(Long id) {
        return cuentaOutPort.buscarPorId(id)
                .orElseThrow(() -> new CuentaNoEncontradoException(
                        "No existe una cuenta con el id: " + id));
    }

    private void validarNumeroCuentaDisponible(String numeroCuenta, Long idActual) {
        Optional<Cuenta> porNumero = cuentaOutPort.buscarPorNumeroCuenta(numeroCuenta.trim());
        boolean esOtraCuenta = porNumero.isPresent()
                && (idActual == null || !porNumero.get().getId().equals(idActual));
        if (esOtraCuenta) {
            throw new NumeroCuentaDuplicadoException(
                    "Ya existe una cuenta con el numero: " + numeroCuenta);
        }
    }

    /** Default de negocio: si el DTO no trae estado, la cuenta queda INACTIVO. */
    private EstadoCuenta resolverEstado(EstadoCuenta estado) {
        return estado != null ? estado : EstadoCuenta.INACTIVO;
    }

    /** Convierte el estado en texto (path) al enum del dominio (400 si es invalido). */
    private EstadoCuenta parseEstado(String estado) {
        if (estado == null || estado.isBlank()) {
            return EstadoCuenta.INACTIVO; // default de negocio
        }
        try {
            return EstadoCuenta.valueOf(estado.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException(
                    "Estado invalido: '" + estado + "'. Valores permitidos: ACTIVO, INACTIVO");
        }
    }

    /** Convierte el modelo de dominio a DTO de salida (nunca se expone la entidad). */
    private CuentaDtoResponse aResponse(Cuenta cuenta) {
        CuentaDtoResponse r = new CuentaDtoResponse();
        r.setId(cuenta.getId());
        r.setNumeroCuenta(cuenta.getNumeroCuenta());
        r.setClienteId(cuenta.getClienteId());
        r.setSaldo(cuenta.getSaldo());
        r.setMoneda(cuenta.getMoneda());
        r.setEstado(cuenta.getEstado() != null ? cuenta.getEstado() : EstadoCuenta.INACTIVO);
        return r;
    }
}