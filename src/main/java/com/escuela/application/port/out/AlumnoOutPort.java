package com.escuela.application.port.out;

import com.escuela.domain.model.Alumno;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de SALIDA: contrato de persistencia de alumnos.
 * Se expresa en terminos del MODELO DE DOMINIO (Alumno), nunca de JPA.
 * Lo implementa el adaptador de base de datos (infrastructure/out/db/AlumnoAdapter).
 */
public interface AlumnoOutPort {

    /** Guarda un alumno nuevo o actualiza uno existente. */
    Alumno guardar(Alumno alumno);

    /** Busca un alumno por id. */
    Optional<Alumno> buscarPorId(Long id);

    /** Busca un alumno por email (unico). */
    Optional<Alumno> buscarPorEmail(String email);

    /** Lista todos los alumnos. */
    List<Alumno> listarTodos();

    /** Lista alumnos por estado. */
    List<Alumno> listarPorEstado(Alumno.Estado estado);

    /** Elimina un alumno. */
    void eliminar(Alumno alumno);
}
