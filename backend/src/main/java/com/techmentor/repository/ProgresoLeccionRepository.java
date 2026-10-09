package com.techmentor.repository;

import com.techmentor.entity.ProgresoLeccion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProgresoLeccionRepository extends JpaRepository<ProgresoLeccion, Long> {

    @Query("SELECT p FROM ProgresoLeccion p WHERE (:idUsuario IS NULL OR p.usuario.idUsuario = :idUsuario) AND (:idLeccion IS NULL OR p.leccion.idLeccion = :idLeccion)")
    Page<ProgresoLeccion> buscar(@Param("idUsuario") Long idUsuario, @Param("idLeccion") Long idLeccion, Pageable pageable);

    List<ProgresoLeccion> findByUsuarioIdUsuario(Long idUsuario);

    Optional<ProgresoLeccion> findFirstByUsuarioIdUsuarioOrderByFechaCompletadoDesc(Long idUsuario);

    List<ProgresoLeccion> findTop5ByOrderByFechaCompletadoDesc();

    @Query("SELECT c.nombre AS curso, COUNT(DISTINCT p.usuario.idUsuario) AS alumnos FROM ProgresoLeccion p JOIN p.leccion l JOIN l.nivel n JOIN n.curso c GROUP BY c.idCurso, c.nombre")
    List<Object[]> countAlumnosPorCurso();

    @Query("SELECT p FROM ProgresoLeccion p WHERE p.usuario.idUsuario = :idUsuario AND p.fechaCompletado >= :desde AND p.fechaCompletado <= :hasta ORDER BY p.fechaCompletado ASC")
    List<ProgresoLeccion> findByUsuarioAndRangoFechas(@Param("idUsuario") Long idUsuario, @Param("desde") OffsetDateTime desde, @Param("hasta") OffsetDateTime hasta);
}
