package com.example.techmentor.persistence;

import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.techmentor.bean.entity.*;

/** Repositorio de credenciales de acceso (username + hash). Carga usuario, rol y estado en una sola consulta. */
@Repository
public interface CredencialUsuarioRepository extends JpaRepository<CredencialUsuario, Long> {

    @EntityGraph(attributePaths = {"usuario", "usuario.rol", "usuario.estadoUsuario"})
    Optional<CredencialUsuario> findByUsername(String username);

    boolean existsByUsername(String username);

    Optional<CredencialUsuario> findByUsuario_IdUsuario(Long idUsuario);
}
