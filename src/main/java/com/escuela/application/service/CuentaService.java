package com.escuela.application.service;

import com.escuela.application.dto.ClienteDTO;
import com.escuela.application.dto.ClienteDtoResponse;
import com.escuela.application.port.in.ClienteUseCase;
import com.escuela.application.port.out.ClienteOutPort;
import com.escuela.domain.exception.ClienteNoEncontradoException;
import com.escuela.domain.exception.EmailDuplicadoException;
import com.escuela.domain.model.Cliente;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio de aplicacion: implementa el puerto de entrada ClienteUseCase.
 *
 * Regla hexagonal: SOLO conoce el dominio y sus propios puertos.
 * Inyecta la interfaz ClienteOutPort (puerto de salida), nunca clases
 * concretas de infraestructura (JPA, web, seguridad).
 */
@Service
public class CuentaService implements CuentaUseCase {

    private final ClienteOutPort clienteOutPort;

    public CuentaService(ClienteOutPort clienteOutPort) {
        this.clienteOutPort = clienteOutPort;
    }

    @Override
    public ClienteDtoResponse crear(ClienteDTO dto) {
        validarEmailDisponible(dto.getEmail(), null);
        Cliente cliente = new Cliente();
        cliente.setNombre(dto.getNombre().trim());
        cliente.setApellido(dto.getApellido().trim());
        cliente.setEmail(normalizarEmail(dto.getEmail()));
        cliente.setTelefono(dto.getTelefono());
        cliente.setEstado(parseEstado(dto.getEstado()));
        cliente.setFechaInscripcion(LocalDateTime.now());
        Cliente guardado = clienteOutPort.guardar(cliente);
        return aResponse(guardado);
    }

    @Override
    public ClienteDtoResponse obtenerPorId(Long id) {
        return aResponse(buscarCliente(id));
    }

    @Override
    public List<ClienteDtoResponse> listarTodos() {
        return clienteOutPort.listarTodos().stream()
                .map(this::aResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ClienteDtoResponse> listarPorEstado(String estado) {
        return clienteOutPort.listarPorEstado(parseEstado(estado)).stream()
                .map(this::aResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ClienteDtoResponse actualizar(Long id, ClienteDTO dto) {
        Cliente existente = buscarCliente(id);
        validarEmailDisponible(dto.getEmail(), id);
        existente.setNombre(dto.getNombre().trim());
        existente.setApellido(dto.getApellido().trim());
        existente.setEmail(normalizarEmail(dto.getEmail()));
        existente.setTelefono(dto.getTelefono());
        existente.setEstado(parseEstado(dto.getEstado()));
        // fechaInscripcion se conserva: no se modifica al actualizar
        return aResponse(clienteOutPort.guardar(existente));
    }

    @Override
    public void eliminar(Long id) {
        Cliente existente = buscarCliente(id);
        clienteOutPort.eliminar(existente);
    }

    // ------------------------------------------------------------------
    // Reglas de negocio privadas
    // ------------------------------------------------------------------

    private Cliente buscarCliente(Long id) {
        return clienteOutPort.buscarPorId(id)
                .orElseThrow(() -> new ClienteNoEncontradoException(
                        "No existe un cliente con el id: " + id));
    }

    private void validarEmailDisponible(String email, Long idActual) {
        Optional<Cliente> porEmail = clienteOutPort.buscarPorEmail(normalizarEmail(email));
        boolean esOtroCliente = porEmail.isPresent()
                && (idActual == null || !porEmail.get().getId().equals(idActual));
        if (esOtroCliente) {
            throw new EmailDuplicadoException("Ya existe un cliente con el email: " + email);
        }
    }

    private String normalizarEmail(String email) {
        return email == null ? null : email.trim().toLowerCase();
    }

    private Cliente.Estado parseEstado(String estado) {
        if (estado == null || estado.isBlank()) {
            return Cliente.Estado.ACTIVO; // default de negocio
        }
        try {
            return Cliente.Estado.valueOf(estado.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException(
                    "Estado invalido: '" + estado + "'. Valores permitidos: ACTIVO, INACTIVO");
        }
    }

    /** Convierte el modelo de dominio a DTO de salida (nunca se expone la entidad). */
    private ClienteDtoResponse aResponse(Cliente cliente) {
        ClienteDtoResponse r = new ClienteDtoResponse();
        r.setId(cliente.getId());
        r.setNombre(cliente.getNombre());
        r.setApellido(cliente.getApellido());
        r.setEmail(cliente.getEmail());
        r.setTelefono(cliente.getTelefono());
        r.setEstado(cliente.getEstado() != null ? cliente.getEstado().name() : Cliente.Estado.ACTIVO.name());
        r.setFechaInscripcion(cliente.getFechaInscripcion());
        return r;
    }
}
