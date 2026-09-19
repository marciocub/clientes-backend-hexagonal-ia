package com.escuela.infrastructure.in;

import com.escuela.application.dto.AlumnoDTO;
import com.escuela.application.dto.AlumnoDtoResponse;
import com.escuela.application.port.in.AlumnoUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Adaptador REST (entrada) para el ABM de alumnos.
 * Inyecta el PUERTO DE ENTRADA AlumnoUseCase (nunca el service concreto,
 * y jamas la capa de persistencia). Rutas protegidas por JWT.
 */
@RestController
@RequestMapping("/api/alumnos")
public class AlumnoController {

    private final AlumnoUseCase alumnoUseCase;

    public AlumnoController(AlumnoUseCase alumnoUseCase) {
        this.alumnoUseCase = alumnoUseCase;
    }

    /** GET /api/alumnos - Lista todos los alumnos. */
    @GetMapping
    public ResponseEntity<List<AlumnoDtoResponse>> listarTodos() {
        return ResponseEntity.ok(alumnoUseCase.listarTodos());
    }

    /** GET /api/alumnos/{id} - Obtiene un alumno por id (404 si no existe). */
    @GetMapping("/{id}")
    public ResponseEntity<AlumnoDtoResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(alumnoUseCase.obtenerPorId(id));
    }

    /** GET /api/alumnos/estado/{estado} - Filtra por ACTIVO / INACTIVO. */
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<AlumnoDtoResponse>> listarPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(alumnoUseCase.listarPorEstado(estado));
    }

    /** POST /api/alumnos - Crea un alumno (201, 400 validaciones, 409 email duplicado). */
    @PostMapping
    public ResponseEntity<AlumnoDtoResponse> crear(@Valid @RequestBody AlumnoDTO dto) {
        AlumnoDtoResponse creado = alumnoUseCase.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    /** PUT /api/alumnos/{id} - Actualiza un alumno (404 si no existe). */
    @PutMapping("/{id}")
    public ResponseEntity<AlumnoDtoResponse> actualizar(@PathVariable Long id,
                                                        @Valid @RequestBody AlumnoDTO dto) {
        return ResponseEntity.ok(alumnoUseCase.actualizar(id, dto));
    }

    /** DELETE /api/alumnos/{id} - Elimina un alumno (204). */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        alumnoUseCase.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
