package com.example.techmentor.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.techmentor.bean.entity.Curso;

/** Repositorio JPA de curso: consultas a la tabla `cursos`. */
@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {

    List<Curso> findByActivoTrue();
    List<Curso> findByActivoTrueAndCategoria_IdCategoria(Long idCategoria);
}
