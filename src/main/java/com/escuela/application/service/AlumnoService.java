package com.escuela.application.service;

import com.escuela.application.dto.AlumnoDTO;
import com.escuela.application.dto.AlumnoDtoResponse;
import com.escuela.application.port.in.AlumnoUseCase;
import com.escuela.application.port.out.AlumnoOutPort;
import com.escuela.domain.exception.AlumnoNoEncontradoException;
import com.escuela.domain.exception.EmailDuplicadoException;
import com.escuela.domain.model.Alumno;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio de aplicacion: implementa el puerto de entrada AlumnoUseCase.
 *
 * Regla hexagonal: SOLO conoce el dominio y sus propios puertos.
 * Inyecta la interfaz AlumnoOutPort (puerto de salida), nunca clases
 * concretas de infraestructura (JPA, web, seguridad).
 */
@Service
public class AlumnoService implements AlumnoUseCase {

    private final AlumnoOutPort alumnoOutPort;

    public AlumnoService(AlumnoOutPort alumnoOutPort) {
        this.alumnoOutPort = alumnoOutPort;
    }

    @Override
    public AlumnoDtoResponse crear(AlumnoDTO dto) {
        validarEmailDisponible(dto.getEmail(), null);
        Alumno alumno = new Alumno();
        alumno.setNombre(dto.getNombre().trim());
        alumno.setApellido(dto.getApellido().trim());
        alumno.setEmail(normalizarEmail(dto.getEmail()));
        alumno.setTelefono(dto.getTelefono());
        alumno.setEstado(parseEstado(dto.getEstado()));
        alumno.setFechaInscripcion(LocalDateTime.now());
        Alumno guardado = alumnoOutPort.guardar(alumno);
        return aResponse(guardado);
    }

    @Override
    public AlumnoDtoResponse obtenerPorId(Long id) {
        return aResponse(buscarAlumno(id));
    }

    @Override
    public List<AlumnoDtoResponse> listarTodos() {
        return alumnoOutPort.listarTodos().stream()
                .map(this::aResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<AlumnoDtoResponse> listarPorEstado(String estado) {
        return alumnoOutPort.listarPorEstado(parseEstado(estado)).stream()
                .map(this::aResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AlumnoDtoResponse actualizar(Long id, AlumnoDTO dto) {
        Alumno existente = buscarAlumno(id);
        validarEmailDisponible(dto.getEmail(), id);
        existente.setNombre(dto.getNombre().trim());
        existente.setApellido(dto.getApellido().trim());
        existente.setEmail(normalizarEmail(dto.getEmail()));
        existente.setTelefono(dto.getTelefono());
        existente.setEstado(parseEstado(dto.getEstado()));
        // fechaInscripcion se conserva: no se modifica al actualizar
        return aResponse(alumnoOutPort.guardar(existente));
    }

    @Override
    public void eliminar(Long id) {
        Alumno existente = buscarAlumno(id);
        alumnoOutPort.eliminar(existente);
    }

    // ------------------------------------------------------------------
    // Reglas de negocio privadas
    // ------------------------------------------------------------------

    private Alumno buscarAlumno(Long id) {
        return alumnoOutPort.buscarPorId(id)
                .orElseThrow(() -> new AlumnoNoEncontradoException(
                        "No existe un alumno con el id: " + id));
    }

    private void validarEmailDisponible(String email, Long idActual) {
        Optional<Alumno> porEmail = alumnoOutPort.buscarPorEmail(normalizarEmail(email));
        boolean esOtroAlumno = porEmail.isPresent()
                && (idActual == null || !porEmail.get().getId().equals(idActual));
        if (esOtroAlumno) {
            throw new EmailDuplicadoException("Ya existe un alumno con el email: " + email);
        }
    }

    private String normalizarEmail(String email) {
        return email == null ? null : email.trim().toLowerCase();
    }

    private Alumno.Estado parseEstado(String estado) {
        if (estado == null || estado.isBlank()) {
            return Alumno.Estado.ACTIVO; // default de negocio
        }
        try {
            return Alumno.Estado.valueOf(estado.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException(
                    "Estado invalido: '" + estado + "'. Valores permitidos: ACTIVO, INACTIVO");
        }
    }

    /** Convierte el modelo de dominio a DTO de salida (nunca se expone la entidad). */
    private AlumnoDtoResponse aResponse(Alumno alumno) {
        AlumnoDtoResponse r = new AlumnoDtoResponse();
        r.setId(alumno.getId());
        r.setNombre(alumno.getNombre());
        r.setApellido(alumno.getApellido());
        r.setEmail(alumno.getEmail());
        r.setTelefono(alumno.getTelefono());
        r.setEstado(alumno.getEstado() != null ? alumno.getEstado().name() : Alumno.Estado.ACTIVO.name());
        r.setFechaInscripcion(alumno.getFechaInscripcion());
        return r;
    }
}
