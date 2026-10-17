package com.example.techmentor.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.techmentor.bean.entity.*;

/** Repositorio JPA de pregunta diagnostico. */
@Repository
public interface PreguntaDiagnosticoRepository extends JpaRepository<PreguntaDiagnostico, Long> {

    List<PreguntaDiagnostico> findByExamen_IdExamenOrderByOrdenAsc(Long idExamen);
}
