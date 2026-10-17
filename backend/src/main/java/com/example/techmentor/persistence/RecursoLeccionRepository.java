package com.example.techmentor.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.techmentor.bean.entity.RecursoLeccion;

/** Repositorio JPA de recurso de lección: consultas a la tabla `recursos_leccion`. */
@Repository
public interface RecursoLeccionRepository extends JpaRepository<RecursoLeccion, Long> {

    List<RecursoLeccion> findByLeccion_IdLeccionOrderByOrdenAsc(Long idLeccion);
    List<RecursoLeccion> findByLeccion_IdLeccionAndActivoTrueOrderByOrdenAsc(Long idLeccion);
}
