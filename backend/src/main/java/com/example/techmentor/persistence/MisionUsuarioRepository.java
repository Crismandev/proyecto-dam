package com.example.techmentor.persistence;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.techmentor.bean.entity.MisionUsuario;

/** Repositorio JPA de misiones de un usuario. */
@Repository
public interface MisionUsuarioRepository extends JpaRepository<MisionUsuario, Long> {
    List<MisionUsuario> findByUsuario_IdUsuario(Long idUsuario);
    boolean existsByUsuario_IdUsuarioAndMision_IdMision(Long idUsuario, Long idMision);

    /** Obtiene solo el ID para bloquear al dueño antes de cargar el progreso y su saldo. */
    @Query("select mu.usuario.idUsuario from MisionUsuario mu where mu.idMisionUsuario = :idMisionUsuario")
    Optional<Long> buscarIdUsuario(@Param("idMisionUsuario") Long idMisionUsuario);
}
