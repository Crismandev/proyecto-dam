package com.example.techmentor.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.techmentor.bean.entity.*;

/** Repositorio JPA de categoria pregunta. */
@Repository
public interface CategoriaPreguntaRepository extends JpaRepository<CategoriaPregunta, Long> {
}
