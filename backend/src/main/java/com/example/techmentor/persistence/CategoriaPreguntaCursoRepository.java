package com.example.techmentor.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.techmentor.bean.entity.CategoriaPreguntaCurso;

/** Repositorio JPA de relación categoría de pregunta-curso: consultas a la tabla `categoria_pregunta_cursos`. */
@Repository
public interface CategoriaPreguntaCursoRepository extends JpaRepository<CategoriaPreguntaCurso, Long> {

    List<CategoriaPreguntaCurso> findByCategoriaPregunta_IdCategoriaPregunta(Long idCategoriaPregunta);
}
