package com.example.techmentor.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.techmentor.bean.entity.*;

/** Repositorio JPA de respuesta diagnostico. */
@Repository
public interface RespuestaDiagnosticoRepository extends JpaRepository<RespuestaDiagnostico, Long> {

    List<RespuestaDiagnostico> findByResultado_IdResultado(Long idResultado);
}
