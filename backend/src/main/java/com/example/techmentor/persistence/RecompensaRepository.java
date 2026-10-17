package com.example.techmentor.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.techmentor.bean.entity.Recompensa;

/** Repositorio JPA de recompensa: consultas a la tabla `recompensas`. */
@Repository
public interface RecompensaRepository extends JpaRepository<Recompensa, Long> {

    List<Recompensa> findByActivaTrue();
}
