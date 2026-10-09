package com.techmentor.repository;

import com.techmentor.entity.Categoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    Optional<Categoria> findByNombre(String nombre);
    boolean existsByNombre(String nombre);

    default Optional<Categoria> findByNombreCategoria(String nombreCategoria) {
        return findByNombre(nombreCategoria);
    }
    default boolean existsByNombreCategoria(String nombreCategoria) {
        return existsByNombre(nombreCategoria);
    }

    @Query("SELECT c FROM Categoria c WHERE LOWER(c.nombre) LIKE LOWER(CONCAT('%', :q, '%')) OR LOWER(COALESCE(c.descripcion, '')) LIKE LOWER(CONCAT('%', :q, '%'))")
    Page<Categoria> buscar(@Param("q") String q, Pageable pageable);
}
