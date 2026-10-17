package com.example.techmentor.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.techmentor.bean.entity.OpcionEjercicio;

/** Repositorio JPA de opción de ejercicio: consultas a la tabla `opciones_ejercicio`. */
@Repository
public interface OpcionEjercicioRepository extends JpaRepository<OpcionEjercicio, Long> {

    List<OpcionEjercicio> findByEjercicio_IdEjercicioOrderByOrdenAsc(Long idEjercicio);
}
