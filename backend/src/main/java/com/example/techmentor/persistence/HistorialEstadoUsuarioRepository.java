package com.example.techmentor.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.techmentor.bean.entity.HistorialEstadoUsuario;

/** Repositorio JPA de historial de estados de usuario. */
@Repository
public interface HistorialEstadoUsuarioRepository extends JpaRepository<HistorialEstadoUsuario, Long> {
    List<HistorialEstadoUsuario> findByUsuario_IdUsuarioOrderByFechaCambioDesc(Long idUsuario);
}
