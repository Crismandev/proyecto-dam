package com.example.techmentor.persistence;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.techmentor.bean.entity.*;

/** Repositorio JPA de resultado diagnostico. */
@Repository
public interface ResultadoDiagnosticoRepository extends JpaRepository<ResultadoDiagnostico, Long> {

    List<ResultadoDiagnostico> findByUsuario_IdUsuarioOrderByFechaRealizacionDesc(Long idUsuario);

    List<ResultadoDiagnostico> findByExamen_IdExamenOrderByFechaRealizacionDesc(Long idExamen);

    /** Último resultado del usuario en exámenes de un curso que asignó un nivel. */
    Optional<ResultadoDiagnostico>
            findFirstByUsuario_IdUsuarioAndExamen_Curso_IdCursoAndNivelAsignadoIsNotNullOrderByFechaRealizacionDesc(
                    Long idUsuario, Long idCurso);
}
