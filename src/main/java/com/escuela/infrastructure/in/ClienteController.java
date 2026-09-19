package com.escuela.infrastructure.in;

import com.escuela.application.dto.ClienteDTO;
import com.escuela.application.dto.ClienteDtoResponse;
import com.escuela.application.port.in.ClienteUseCase;
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
 * Adaptador REST (entrada) para el ABM de clientes.
 * Inyecta el PUERTO DE ENTRADA ClienteUseCase (nunca el service concreto,
 * y jamas la capa de persistencia). Rutas protegidas por JWT.
 */
@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteUseCase clienteUseCase;

    public ClienteController(ClienteUseCase clienteUseCase) {
        this.clienteUseCase = clienteUseCase;
    }

    /** GET /api/clientes - Lista todos los clientes. */
    @GetMapping
    public ResponseEntity<List<ClienteDtoResponse>> listarTodos() {
        return ResponseEntity.ok(clienteUseCase.listarTodos());
    }

    /** GET /api/clientes/{id} - Obtiene un cliente por id (404 si no existe). */
    @GetMapping("/{id}")
    public ResponseEntity<ClienteDtoResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(clienteUseCase.obtenerPorId(id));
    }

    /** GET /api/clientes/estado/{estado} - Filtra por ACTIVO / INACTIVO. */
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<ClienteDtoResponse>> listarPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(clienteUseCase.listarPorEstado(estado));
    }

    /** POST /api/clientes - Crea un cliente (201, 400 validaciones, 409 email duplicado). */
    @PostMapping
    public ResponseEntity<ClienteDtoResponse> crear(@Valid @RequestBody ClienteDTO dto) {
        ClienteDtoResponse creado = clienteUseCase.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    /** PUT /api/clientes/{id} - Actualiza un cliente (404 si no existe). */
    @PutMapping("/{id}")
    public ResponseEntity<ClienteDtoResponse> actualizar(@PathVariable Long id,
                                                        @Valid @RequestBody ClienteDTO dto) {
        return ResponseEntity.ok(clienteUseCase.actualizar(id, dto));
    }

    /** DELETE /api/clientes/{id} - Elimina un cliente (204). */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        clienteUseCase.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
