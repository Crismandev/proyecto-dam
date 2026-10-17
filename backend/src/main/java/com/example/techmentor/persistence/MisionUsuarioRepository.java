package com.example.techmentor.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.techmentor.bean.entity.MisionUsuario;

/** Repositorio JPA de misiones de un usuario. */
@Repository
public interface MisionUsuarioRepository extends JpaRepository<MisionUsuario, Long> {
    List<MisionUsuario> findByUsuario_IdUsuario(Long idUsuario);
    boolean existsByUsuario_IdUsuarioAndMision_IdMision(Long idUsuario, Long idMision);
}
