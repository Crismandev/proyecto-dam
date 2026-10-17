package com.example.techmentor.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.techmentor.bean.entity.RecompensaUsuario;

/** Repositorio JPA de recompensas de un usuario. */
@Repository
public interface RecompensaUsuarioRepository extends JpaRepository<RecompensaUsuario, Long> {
    List<RecompensaUsuario> findByUsuario_IdUsuarioOrderByFechaObtencionDesc(Long idUsuario);
    boolean existsByUsuario_IdUsuarioAndRecompensa_IdRecompensa(Long idUsuario, Long idRecompensa);
}
