package com.banco.application.service;

import com.banco.application.dto.LoginRequest;
import com.banco.application.dto.LoginResponse;
import com.banco.application.dto.RegistroRequest;
import com.banco.application.port.in.AuthUseCase;
import com.banco.application.port.out.PasswordEncoderPort;
import com.banco.application.port.out.TokenProviderPort;
import com.banco.application.port.out.UsuarioOutPort;
import com.banco.domain.exception.CredencialesInvalidasException;
import com.banco.domain.exception.EmailDuplicadoException;
import com.banco.domain.model.Rol;
import com.banco.domain.model.Usuario;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Servicio de aplicacion: implementa el puerto de entrada AuthUseCase.
 *
 * Regla hexagonal: inyecta SOLO puertos de salida (UsuarioOutPort,
 * PasswordEncoderPort, TokenProviderPort). No sabe si el hash es BCrypt
 * ni como se firma el JWT: eso es detalle de infraestructura.
 */
@Service
public class AuthService implements AuthUseCase {

    private final UsuarioOutPort usuarioOutPort;
    private final PasswordEncoderPort passwordEncoderPort;
    private final TokenProviderPort tokenProviderPort;

    public AuthService(UsuarioOutPort usuarioOutPort,
                       PasswordEncoderPort passwordEncoderPort,
                       TokenProviderPort tokenProviderPort) {
        this.usuarioOutPort = usuarioOutPort;
        this.passwordEncoderPort = passwordEncoderPort;
        this.tokenProviderPort = tokenProviderPort;
    }

    @Override
    public LoginResponse registrar(RegistroRequest request) {
        String email = request.getEmail().trim().toLowerCase();

        if (usuarioOutPort.existePorEmail(email)) {
            throw new EmailDuplicadoException("Ya existe un usuario registrado con el email: " + email);
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre().trim());
        usuario.setEmail(email);
        // La contrasena NUNCA se guarda en texto plano: se hashea con el puerto
        usuario.setPasswordHash(passwordEncoderPort.hashear(request.getPassword()));
        usuario.setRol(Rol.USER);   // default de negocio
        usuario.setActivo(true);    // default de negocio
        usuario.setFechaCreacion(LocalDateTime.now());

        Usuario guardado = usuarioOutPort.guardar(usuario);
        String token = tokenProviderPort.generarToken(guardado.getEmail());

        return new LoginResponse(token, guardado.getEmail(), guardado.getNombre(),
                "Usuario registrado correctamente");
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        String email = request.getEmail().trim().toLowerCase();

        Usuario usuario = usuarioOutPort.buscarPorEmail(email)
                .orElseThrow(() -> new CredencialesInvalidasException("Email o contrasena incorrectos"));

        if (!passwordEncoderPort.verificar(request.getPassword(), usuario.getPasswordHash())) {
            throw new CredencialesInvalidasException("Email o contrasena incorrectos");
        }

        if (!usuario.isActivo()) {
            throw new CredencialesInvalidasException("El usuario se encuentra deshabilitado");
        }

        String token = tokenProviderPort.generarToken(usuario.getEmail());
        return new LoginResponse(token, usuario.getEmail(), usuario.getNombre(), "Login correcto");
    }
}
