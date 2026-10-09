package com.techmentor.repository;

import com.techmentor.entity.Nivel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NivelRepository extends JpaRepository<Nivel, Long> {

    @Query("SELECT n FROM Nivel n WHERE (:idCurso IS NULL OR n.curso.idCurso = :idCurso) AND (LOWER(n.nombre) LIKE LOWER(CONCAT('%', :q, '%')) OR LOWER(COALESCE(n.descripcion, '')) LIKE LOWER(CONCAT('%', :q, '%')))")
    Page<Nivel> buscar(@Param("idCurso") Long idCurso, @Param("q") String q, Pageable pageable);

    List<Nivel> findByCursoIdCursoOrderByOrdenAsc(Long idCurso);
}
