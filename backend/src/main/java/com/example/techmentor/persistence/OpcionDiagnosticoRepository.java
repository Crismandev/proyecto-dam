package com.example.techmentor.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.techmentor.bean.entity.*;

/** Repositorio JPA de opcion diagnostico. */
@Repository
public interface OpcionDiagnosticoRepository extends JpaRepository<OpcionDiagnostico, Long> {

    List<OpcionDiagnostico> findByPregunta_IdPreguntaOrderByOrdenAsc(Long idPregunta);

    /** Todas las opciones de un examen (con su pregunta) en una sola consulta. */
    @Query("select o from OpcionDiagnostico o join fetch o.pregunta p "
            + "where p.examen.idExamen = :idExamen order by p.orden, o.orden")
    List<OpcionDiagnostico> findAllByExamen(@Param("idExamen") Long idExamen);
}
