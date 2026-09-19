package com.escuela.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Configuracion de seguridad (adaptador de infraestructura):
 * - API stateless: sin sesiones, sin cookies.
 * - CSRF deshabilitado (no hay estado de sesion que proteger).
 * - CORS habilitado para http://localhost:3000.
 * - Rutas PUBLICAS: POST /api/usuarios/registro, POST /api/usuarios/login, GET /api/usuarios/ping.
 * - Rutas PROTEGIDAS: todas las demas (/api/clientes/**) requieren token valido.
 * - Sin token -&gt; 403 (entry point por defecto de Spring Security);
 *   token invalido/expirado -&gt; 401 (lo resuelve el JwtAuthenticationFilter).
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Rutas publicas
                        .requestMatchers(HttpMethod.POST, "/api/usuarios/registro", "/api/usuarios/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/usuarios/ping").permitAll()
                        // Todas las demas rutas requieren token valido
                        .anyRequest().authenticated())
                // El filtro JWT corre antes del filtro de autenticacion estandar
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /** CORS para el frontend React (necesario para que los preflight OPTIONS no fallan). */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuracion = new CorsConfiguration();
        configuracion.setAllowedOrigins(List.of("http://localhost:3000"));
        configuracion.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuracion.setAllowedHeaders(List.of("*"));
        configuracion.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource fuente = new UrlBasedCorsConfigurationSource();
        fuente.registerCorsConfiguration("/**", configuracion);
        return fuente;
    }
}
