package com.techmentor.repository;

import com.techmentor.entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    boolean existsByEmail(String email);

    default Optional<Usuario> findByCorreo(String correo) {
        return findByIdentificador(correo);
    }

    @Query("SELECT u FROM Usuario u WHERE u.email = :identifier OR u.idUsuario IN (SELECT c.usuario.idUsuario FROM CredencialUsuario c WHERE c.username = :identifier)")
    Optional<Usuario> findByIdentificador(@Param("identifier") String identifier);

    @Query("SELECT u FROM Usuario u WHERE " +
           "LOWER(u.nombres) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
           "LOWER(COALESCE(u.apellidos, '')) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
           "LOWER(u.email) LIKE LOWER(CONCAT('%', :q, '%'))")
    Page<Usuario> buscar(@Param("q") String q, Pageable pageable);

    long countByRolNombre(String nombre);

    List<Usuario> findTop5ByOrderByFechaRegistroDesc();
}
