package com.techmentor.repository;

import com.techmentor.entity.OpcionEjercicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OpcionEjercicioRepository extends JpaRepository<OpcionEjercicio, Long> {
    List<OpcionEjercicio> findByEjercicioIdEjercicio(Long idEjercicio);
}
