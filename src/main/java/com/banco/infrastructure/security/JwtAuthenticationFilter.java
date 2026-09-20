package com.banco.infrastructure.security;

import com.banco.application.port.out.TokenProviderPort;
import com.banco.application.port.out.UsuarioOutPort;
import com.banco.domain.model.Usuario;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Filtro JWT: se ejecuta en cada request antes del SecurityFilterChain.
 *
 * - Sin header Authorization -&gt; sigue la cadena; las rutas protegidas responden 403.
 * - Con "Bearer &lt;token&gt;" valido -&gt; valida con TokenProviderPort, carga el usuario,
 *   setea la autenticacion en el SecurityContext y sigue la cadena.
 * - Con token invalido o expirado -&gt; responde 401 y corta la cadena.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProviderPort tokenProviderPort;
    private final UsuarioOutPort usuarioOutPort;

    public JwtAuthenticationFilter(TokenProviderPort tokenProviderPort,
                                   UsuarioOutPort usuarioOutPort) {
        this.tokenProviderPort = tokenProviderPort;
        this.usuarioOutPort = usuarioOutPort;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            // No hay token: se deja pasar. La autorizacion decidira (403 en rutas protegidas).
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);

        if (!tokenProviderPort.validarToken(token)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(
                    "{\"status\":401,\"error\":\"Unauthorized\",\"mensaje\":\"Token invalido o expirado\"}");
            return;
        }

        String email = tokenProviderPort.extraerEmail(token);

        // Carga el usuario para verificar que sigue existiendo y activo
        Usuario usuario = usuarioOutPort.buscarPorEmail(email).orElse(null);
        if (usuario == null || !usuario.isActivo()) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(
                    "{\"status\":401,\"error\":\"Unauthorized\",\"mensaje\":\"Usuario inexistente o deshabilitado\"}");
            return;
        }

        List<SimpleGrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name()));

        UsernamePasswordAuthenticationToken autenticacion =
                new UsernamePasswordAuthenticationToken(usuario.getEmail(), null, authorities);
        SecurityContextHolder.getContext().setAuthentication(autenticacion);

        filterChain.doFilter(request, response);
    }
}
