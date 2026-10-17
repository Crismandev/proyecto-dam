package com.example.techmentor.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.techmentor.bean.entity.Ejercicio;

/** Repositorio JPA de ejercicio: consultas a la tabla `ejercicios`. */
@Repository
public interface EjercicioRepository extends JpaRepository<Ejercicio, Long> {

    List<Ejercicio> findByLeccion_IdLeccionOrderByOrdenAsc(Long idLeccion);
}
