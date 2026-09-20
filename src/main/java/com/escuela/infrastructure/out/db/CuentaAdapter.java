package com.escuela.infrastructure.out.db;

import com.escuela.application.port.out.ClienteOutPort;
import com.escuela.domain.model.Cliente;
import com.escuela.infrastructure.mapper.ClienteJpaMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Adaptador de salida a base de datos: implementa ClienteOutPort.
 * Delega en el ClienteRepository (Spring Data JPA) y usa el mapper
 * para convertir entre dominio y JPA.
 */
@Repository
public class CuentaAdapter implements ClienteOutPort {

    private final ClienteRepository clienteRepository;

    public CuentaAdapter(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public Cliente guardar(Cliente cliente) {
        ClienteJpaEntity guardada = clienteRepository.save(ClienteJpaMapper.aEntity(cliente));
        return ClienteJpaMapper.aDominio(guardada);
    }

    @Override
    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.findById(id).map(ClienteJpaMapper::aDominio);
    }

    @Override
    public Optional<Cliente> buscarPorEmail(String email) {
        return clienteRepository.findByEmail(email).map(ClienteJpaMapper::aDominio);
    }

    @Override
    public List<Cliente> listarTodos() {
        return clienteRepository.findAll().stream()
                .map(ClienteJpaMapper::aDominio)
                .toList();
    }

    @Override
    public List<Cliente> listarPorEstado(Cliente.Estado estado) {
        return clienteRepository.findByEstado(estado.name()).stream()
                .map(ClienteJpaMapper::aDominio)
                .toList();
    }

    @Override
    public void eliminar(Cliente cliente) {
        clienteRepository.deleteById(cliente.getId());
    }
}
