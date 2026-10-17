package com.example.techmentor.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.techmentor.bean.entity.Mision;

/** Repositorio JPA de misión: consultas a la tabla `misiones`. */
@Repository
public interface MisionRepository extends JpaRepository<Mision, Long> {

    List<Mision> findByActivaTrue();
}
