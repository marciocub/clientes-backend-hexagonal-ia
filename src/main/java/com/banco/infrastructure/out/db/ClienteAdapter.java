package com.banco.infrastructure.out.db;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.banco.application.port.out.ClienteOutPort;
import com.banco.domain.model.Cliente;
import com.banco.infrastructure.mapper.ClienteJpaMapper;

/**
 * Adaptador de salida a base de datos: implementa ClienteOutPort.
 * Delega en el ClienteRepository (Spring Data JPA) y usa el mapper
 * para convertir entre dominio y JPA.
 *
 * @Transactional: el mapeo recorre las colecciones LAZY del agregado
 * (tarjetas, facturas, items), por lo que la conversion dominio &lt;-&gt; JPA
 * debe ejecutarse dentro de la misma transaccion.
 */
@Repository
@Transactional
public class ClienteAdapter implements ClienteOutPort {

    private final ClienteRepository clienteRepository;
    private final ClienteJpaMapper clienteJpaMapper;

    public ClienteAdapter(ClienteRepository clienteRepository, ClienteJpaMapper clienteJpaMapper) {
        this.clienteRepository = clienteRepository;
        this.clienteJpaMapper = clienteJpaMapper;
    }

    @Override
    public Cliente guardar(Cliente cliente) {
        ClienteJpaEntity guardado = clienteRepository.save(clienteJpaMapper.aEntity(cliente));
        return clienteJpaMapper.aDominio(guardado);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.findById(id).map(clienteJpaMapper::aDominio);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> listarTodos() {
        return clienteRepository.findAll().stream()
                .map(clienteJpaMapper::aDominio)
                .toList();
    }

    @Override
    public void eliminar(Cliente cliente) {
        // El cascade + orphanRemoval borra tarjetas, facturas e items
        clienteRepository.deleteById(cliente.getId());
    }
}