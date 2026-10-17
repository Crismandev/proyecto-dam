package com.example.techmentor.persistence;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.techmentor.bean.entity.Nivel;

/** Repositorio JPA de nivel: consultas a la tabla `niveles`. */
@Repository
public interface NivelRepository extends JpaRepository<Nivel, Long> {

    List<Nivel> findByCurso_IdCursoOrderByOrdenAsc(Long idCurso);
    Optional<Nivel> findFirstByCurso_IdCursoOrderByOrdenAsc(Long idCurso);
    Optional<Nivel> findFirstByCurso_IdCursoAndOrdenGreaterThanOrderByOrdenAsc(Long idCurso, Integer orden);
}
