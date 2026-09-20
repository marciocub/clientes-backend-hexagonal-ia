package com.banco.infrastructure.out.db;

import com.banco.application.port.out.UsuarioOutPort;
import com.banco.domain.model.Usuario;
import com.banco.infrastructure.mapper.UsuarioJpaMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Adaptador de salida a base de datos: implementa UsuarioOutPort.
 * Delega en el UsuarioRepository (Spring Data JPA) y usa el mapper
 * para convertir entre dominio y JPA.
 */
@Repository
public class UsuarioAdapter implements UsuarioOutPort {

    private final UsuarioRepository usuarioRepository;

    public UsuarioAdapter(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Usuario guardar(Usuario usuario) {
        UsuarioJpaEntity guardada = usuarioRepository.save(UsuarioJpaMapper.aEntity(usuario));
        return UsuarioJpaMapper.aDominio(guardada);
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email).map(UsuarioJpaMapper::aDominio);
    }

    @Override
    public boolean existePorEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }
}
