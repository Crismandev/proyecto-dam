package com.techmentor.repository;

import com.techmentor.entity.Rol;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {
    Optional<Rol> findByNombre(String nombre);

    @Query("SELECT r FROM Rol r WHERE LOWER(r.nombre) LIKE LOWER(CONCAT('%', :q, '%'))")
    Page<Rol> buscar(@Param("q") String q, Pageable pageable);
}
