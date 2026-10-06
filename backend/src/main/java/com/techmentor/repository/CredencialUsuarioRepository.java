package com.techmentor.repository;

import com.techmentor.entity.CredencialUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CredencialUsuarioRepository extends JpaRepository<CredencialUsuario, Long> {
    Optional<CredencialUsuario> findByUsername(String username);
    boolean existsByUsername(String username);
    Optional<CredencialUsuario> findByUsuarioIdUsuario(Long idUsuario);
    Optional<CredencialUsuario> findByUsuarioEmail(String email);
}
