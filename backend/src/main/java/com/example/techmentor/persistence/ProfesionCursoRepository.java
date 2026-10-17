package com.example.techmentor.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.techmentor.bean.entity.ProfesionCurso;

/** Repositorio JPA de relación puesto-curso: consultas a la tabla `profesion_cursos`. */
@Repository
public interface ProfesionCursoRepository extends JpaRepository<ProfesionCurso, Long> {

    List<ProfesionCurso> findByProfesion_IdProfesion(Long idProfesion);
}
