package com.example.techmentor.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.techmentor.bean.entity.RecomendacionCurso;

/** Repositorio JPA de recomendaciones de cursos. */
@Repository
public interface RecomendacionCursoRepository extends JpaRepository<RecomendacionCurso, Long> {
    List<RecomendacionCurso> findByUsuario_IdUsuarioOrderByPrioridadAscFechaCreacionDesc(Long idUsuario);
    List<RecomendacionCurso> findByResultado_IdResultadoOrderByPrioridadAsc(Long idResultado);
    void deleteByResultado_IdResultado(Long idResultado);
}
