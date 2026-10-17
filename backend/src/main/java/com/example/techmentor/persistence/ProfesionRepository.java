package com.example.techmentor.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.techmentor.bean.entity.Profesion;

/** Repositorio JPA de profesión: consultas a la tabla `profesiones`. */
@Repository
public interface ProfesionRepository extends JpaRepository<Profesion, Long> {

    List<Profesion> findByActivoTrue();
}
