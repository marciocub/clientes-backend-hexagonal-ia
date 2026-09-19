package com.escuela.infrastructure.in;

import com.escuela.application.dto.LoginRequest;
import com.escuela.application.dto.LoginResponse;
import com.escuela.application.dto.RegistroRequest;
import com.escuela.application.port.in.AuthUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Adaptador REST (entrada) para autenticacion.
 * Inyecta el PUERTO DE ENTRADA AuthUseCase.
 * Rutas publicas: /registro, /login y /ping.
 */
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final AuthUseCase authUseCase;

    public UsuarioController(AuthUseCase authUseCase) {
        this.authUseCase = authUseCase;
    }

    /** POST /api/usuarios/registro - Registra un usuario y devuelve el token JWT (201). */
    @PostMapping("/registro")
    public ResponseEntity<LoginResponse> registrar(@Valid @RequestBody RegistroRequest request) {
        LoginResponse response = authUseCase.registrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /** POST /api/usuarios/login - Autentica y devuelve el token JWT (200). */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authUseCase.login(request));
    }

    /** GET /api/usuarios/ping - Health check publico. */
    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("pong");
    }
}
