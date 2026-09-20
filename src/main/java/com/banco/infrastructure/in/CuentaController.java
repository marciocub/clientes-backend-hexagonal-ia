package com.banco.infrastructure.in;

import com.banco.application.dto.CuentaDTO;
import com.banco.application.dto.CuentaDtoResponse;
import com.banco.application.port.in.CuentaUseCase;
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
 * Adaptador REST (entrada) para el ABM de cuentas.
 * Inyecta el PUERTO DE ENTRADA CuentaUseCase (nunca el service concreto,
 * y jamas la capa de persistencia). Rutas protegidas por JWT.
 */
@RestController
@RequestMapping("/api/cuentas")
public class CuentaController {

    private final CuentaUseCase cuentaUseCase;

    public CuentaController(CuentaUseCase cuentaUseCase) {
        this.cuentaUseCase = cuentaUseCase;
    }

    /** GET /api/cuentas - Lista todas las cuentas. */
    @GetMapping
    public ResponseEntity<List<CuentaDtoResponse>> listarTodos() {
        return ResponseEntity.ok(cuentaUseCase.listarTodos());
    }

    /** GET /api/cuentas/{id} - Obtiene una cuenta por id (404 si no existe). */
    @GetMapping("/{id}")
    public ResponseEntity<CuentaDtoResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(cuentaUseCase.obtenerPorId(id));
    }

    /** GET /api/cuentas/estado/{estado} - Filtra por ACTIVO / INACTIVO. */
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<CuentaDtoResponse>> listarPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(cuentaUseCase.listarPorEstado(estado));
    }

    /** POST /api/cuentas - Crea una cuenta (201, 400 validaciones, 409 numero duplicado). */
    @PostMapping
    public ResponseEntity<CuentaDtoResponse> crear(@Valid @RequestBody CuentaDTO dto) {
        CuentaDtoResponse creado = cuentaUseCase.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    /** PUT /api/cuentas/{id} - Actualiza una cuenta (404 si no existe). */
    @PutMapping("/{id}")
    public ResponseEntity<CuentaDtoResponse> actualizar(@PathVariable Long id,
                                                        @Valid @RequestBody CuentaDTO dto) {
        return ResponseEntity.ok(cuentaUseCase.actualizar(id, dto));
    }

    /** DELETE /api/cuentas/{id} - Elimina una cuenta (204). */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        cuentaUseCase.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}