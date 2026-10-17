package com.example.techmentor.persistence;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.techmentor.bean.entity.UsuarioProfesion;

/** Repositorio JPA de profesiones de un usuario. */
@Repository
public interface UsuarioProfesionRepository extends JpaRepository<UsuarioProfesion, Long> {
    List<UsuarioProfesion> findByUsuario_IdUsuario(Long idUsuario);
    Optional<UsuarioProfesion> findByUsuario_IdUsuarioAndProfesion_IdProfesion(Long idUsuario, Long idProfesion);
    Optional<UsuarioProfesion> findByUsuario_IdUsuarioAndPrincipalTrue(Long idUsuario);
    boolean existsByUsuario_IdUsuarioAndProfesion_IdProfesion(Long idUsuario, Long idProfesion);
}
