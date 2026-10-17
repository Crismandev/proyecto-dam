package com.example.techmentor.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.techmentor.bean.entity.ExamenDiagnostico;

/** Repositorio JPA de exámenes diagnóstico: filtros por curso y por puesto (profesión). */
@Repository
public interface ExamenDiagnosticoRepository extends JpaRepository<ExamenDiagnostico, Long> {

    List<ExamenDiagnostico> findByActivoTrue();

    List<ExamenDiagnostico> findByActivoTrueAndCurso_IdCurso(Long idCurso);

    List<ExamenDiagnostico> findByActivoTrueAndProfesion_IdProfesion(Long idProfesion);

    List<ExamenDiagnostico> findByActivoTrueAndCurso_IdCursoAndProfesion_IdProfesion(Long idCurso, Long idProfesion);

    /** Exámenes activos generales (sin puesto asignado). */
    List<ExamenDiagnostico> findByActivoTrueAndProfesionIsNull();

    /** Exámenes activos del puesto indicado más los generales. */
    @Query("select e from ExamenDiagnostico e left join e.profesion p "
            + "where e.activo = true and (p is null or p.idProfesion = :idProfesion)")
    List<ExamenDiagnostico> findActivosParaProfesion(@Param("idProfesion") Long idProfesion);
}
