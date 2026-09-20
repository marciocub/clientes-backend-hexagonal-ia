package com.banco.infrastructure.in;

import java.util.List;

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

import com.banco.application.dto.ClienteDTO;
import com.banco.application.dto.ClienteDtoResponse;
import com.banco.application.dto.CrearFacturaDTO;
import com.banco.application.dto.SolicitarTarjetaDTO;
import com.banco.application.port.in.ClienteUseCase;

import jakarta.validation.Valid;

/**
 * Adaptador REST (entrada) para el AGREGADO Cliente (tarjetas + facturas).
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

    /** GET /api/clientes - Lista todos los clientes (agregado completo). */
    @GetMapping
    public ResponseEntity<List<ClienteDtoResponse>> listarTodos() {
        return ResponseEntity.ok(clienteUseCase.listarTodos());
    }

    /** GET /api/clientes/{id} - Obtiene el agregado completo (404 si no existe). */
    @GetMapping("/{id}")
    public ResponseEntity<ClienteDtoResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(clienteUseCase.obtenerPorId(id));
    }

    /** POST /api/clientes - Crea un cliente (201, 400 validaciones). */
    @PostMapping
    public ResponseEntity<ClienteDtoResponse> crearCliente(@Valid @RequestBody ClienteDTO dto) {
        ClienteDtoResponse creado = clienteUseCase.crearCliente(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    /** PUT /api/clientes/{id} - Actualiza nombre/cuit (404 si no existe). */
    @PutMapping("/{id}")
    public ResponseEntity<ClienteDtoResponse> actualizar(@PathVariable Long id,
                                                         @Valid @RequestBody ClienteDTO dto) {
        return ResponseEntity.ok(clienteUseCase.actualizar(id, dto));
    }

    /** DELETE /api/clientes/{id} - Elimina el cliente y su agregado (204). */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        clienteUseCase.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    /** POST /api/clientes/{id}/tarjetas - Solicita tarjeta (201; 409 si excede el tope). */
    @PostMapping("/{id}/tarjetas")
    public ResponseEntity<ClienteDtoResponse> agregarTarjeta(@PathVariable Long id,
                                                             @Valid @RequestBody SolicitarTarjetaDTO dto) {
        ClienteDtoResponse actualizado = clienteUseCase.agregarTarjeta(id, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(actualizado);
    }

    /** POST /api/clientes/{id}/facturas - Emite factura con items (201). */
    @PostMapping("/{id}/facturas")
    public ResponseEntity<ClienteDtoResponse> agregarFactura(@PathVariable Long id,
                                                             @Valid @RequestBody CrearFacturaDTO dto) {
        ClienteDtoResponse actualizado = clienteUseCase.agregarFactura(id, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(actualizado);
    }
}