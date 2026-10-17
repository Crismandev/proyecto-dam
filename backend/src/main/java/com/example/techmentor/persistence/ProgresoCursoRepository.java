package com.example.techmentor.persistence;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.techmentor.bean.entity.ProgresoCurso;

/** Repositorio JPA de progreso curso. */
@Repository
public interface ProgresoCursoRepository extends JpaRepository<ProgresoCurso, Long> {

    Optional<ProgresoCurso> findByUsuario_IdUsuarioAndCurso_IdCurso(Long idUsuario, Long idCurso);

    @Query("select p from ProgresoCurso p where p.usuario.idUsuario = :idUsuario "
            + "order by p.fechaUltimaActividad desc nulls last")
    List<ProgresoCurso> listarPorUsuario(@Param("idUsuario") Long idUsuario);

    List<ProgresoCurso> findByCurso_IdCurso(Long idCurso);
}
