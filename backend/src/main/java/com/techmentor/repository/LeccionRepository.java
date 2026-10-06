package com.techmentor.repository;

import com.techmentor.entity.Leccion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeccionRepository extends JpaRepository<Leccion, Long> {

    @Query("SELECT l FROM Leccion l WHERE (:idNivel IS NULL OR l.nivel.idNivel = :idNivel) AND (LOWER(l.titulo) LIKE LOWER(CONCAT('%', :q, '%')) OR LOWER(COALESCE(l.descripcion, '')) LIKE LOWER(CONCAT('%', :q, '%')))")
    Page<Leccion> buscar(@Param("idNivel") Long idNivel, @Param("q") String q, Pageable pageable);

    List<Leccion> findByNivelIdNivelOrderByOrdenAsc(Long idNivel);
}
