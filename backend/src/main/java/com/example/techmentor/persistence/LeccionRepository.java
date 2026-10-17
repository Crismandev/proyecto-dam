package com.example.techmentor.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.techmentor.bean.entity.Leccion;

/** Repositorio JPA de lección: consultas a la tabla `lecciones`. */
@Repository
public interface LeccionRepository extends JpaRepository<Leccion, Long> {

    List<Leccion> findByNivel_IdNivelOrderByOrdenAsc(Long idNivel);
    List<Leccion> findByNivel_Curso_IdCurso(Long idCurso);
    long countByNivel_Curso_IdCurso(Long idCurso);
    long countByNivel_IdNivel(Long idNivel);
}
