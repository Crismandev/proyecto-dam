package com.techmentor.repository;

import com.techmentor.entity.EstadoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EstadoUsuarioRepository extends JpaRepository<EstadoUsuario, Long> {
    Optional<EstadoUsuario> findByNombre(String nombre);
}
