package com.escuela.infrastructure.mapper;

import com.escuela.domain.model.Alumno;
import com.escuela.infrastructure.out.db.AlumnoJpaEntity;

/**
 * Mapper JPA &lt;-&gt; Dominio para alumnos.
 * La entidad de dominio nunca se persiste ni se expone: se convierte a JPA
 * para persistir y desde JPA hacia dominio al leer.
 */
public final class AlumnoJpaMapper {

    private AlumnoJpaMapper() {
    }

    /** Dominio -&gt; JPA (para guardar). */
    public static AlumnoJpaEntity aEntity(Alumno alumno) {
        AlumnoJpaEntity entity = new AlumnoJpaEntity();
        entity.setId(alumno.getId());
        entity.setNombre(alumno.getNombre());
        entity.setApellido(alumno.getApellido());
        entity.setEmail(alumno.getEmail());
        entity.setTelefono(alumno.getTelefono());
        entity.setEstado(alumno.getEstado() != null ? alumno.getEstado().name() : Alumno.Estado.ACTIVO.name());
        entity.setFechaInscripcion(alumno.getFechaInscripcion());
        return entity;
    }

    /** JPA -&gt; Dominio (para leer). */
    public static Alumno aDominio(AlumnoJpaEntity entity) {
        Alumno alumno = new Alumno();
        alumno.setId(entity.getId());
        alumno.setNombre(entity.getNombre());
        alumno.setApellido(entity.getApellido());
        alumno.setEmail(entity.getEmail());
        alumno.setTelefono(entity.getTelefono());
        alumno.setEstado(entity.getEstado() != null
                ? Alumno.Estado.valueOf(entity.getEstado())
                : Alumno.Estado.ACTIVO);
        alumno.setFechaInscripcion(entity.getFechaInscripcion());
        return alumno;
    }
}
