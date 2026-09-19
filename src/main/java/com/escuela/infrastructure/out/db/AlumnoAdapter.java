package com.escuela.infrastructure.out.db;

import com.escuela.application.port.out.AlumnoOutPort;
import com.escuela.domain.model.Alumno;
import com.escuela.infrastructure.mapper.AlumnoJpaMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Adaptador de salida a base de datos: implementa AlumnoOutPort.
 * Delega en el AlumnoRepository (Spring Data JPA) y usa el mapper
 * para convertir entre dominio y JPA.
 */
@Repository
public class AlumnoAdapter implements AlumnoOutPort {

    private final AlumnoRepository alumnoRepository;

    public AlumnoAdapter(AlumnoRepository alumnoRepository) {
        this.alumnoRepository = alumnoRepository;
    }

    @Override
    public Alumno guardar(Alumno alumno) {
        AlumnoJpaEntity guardada = alumnoRepository.save(AlumnoJpaMapper.aEntity(alumno));
        return AlumnoJpaMapper.aDominio(guardada);
    }

    @Override
    public Optional<Alumno> buscarPorId(Long id) {
        return alumnoRepository.findById(id).map(AlumnoJpaMapper::aDominio);
    }

    @Override
    public Optional<Alumno> buscarPorEmail(String email) {
        return alumnoRepository.findByEmail(email).map(AlumnoJpaMapper::aDominio);
    }

    @Override
    public List<Alumno> listarTodos() {
        return alumnoRepository.findAll().stream()
                .map(AlumnoJpaMapper::aDominio)
                .toList();
    }

    @Override
    public List<Alumno> listarPorEstado(Alumno.Estado estado) {
        return alumnoRepository.findByEstado(estado.name()).stream()
                .map(AlumnoJpaMapper::aDominio)
                .toList();
    }

    @Override
    public void eliminar(Alumno alumno) {
        alumnoRepository.deleteById(alumno.getId());
    }
}
