package com.techmentor.repository;

import com.techmentor.entity.Ejercicio;
import com.techmentor.entity.TipoEjercicio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EjercicioRepository extends JpaRepository<Ejercicio, Long> {

    @Query("SELECT e FROM Ejercicio e WHERE (:idLeccion IS NULL OR e.leccion.idLeccion = :idLeccion) AND (LOWER(e.enunciado) LIKE LOWER(CONCAT('%', :q, '%')))")
    Page<Ejercicio> buscar(@Param("idLeccion") Long idLeccion, @Param("q") String q, Pageable pageable);

    List<Ejercicio> findByLeccionIdLeccionOrderByOrdenAsc(Long idLeccion);

    List<Ejercicio> findByLeccionIdLeccionAndTipoEjercicioOrderByOrdenAsc(Long idLeccion, TipoEjercicio tipoEjercicio);

    @Query("SELECT e FROM Ejercicio e WHERE e.tipoEjercicio = :tipo ORDER BY RANDOM()")
    List<Ejercicio> findRandomByTipo(@Param("tipo") TipoEjercicio tipo, Pageable pageable);
}
