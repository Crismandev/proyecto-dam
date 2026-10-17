package com.example.techmentor.persistence;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.techmentor.bean.entity.EstadoProgreso;
import com.example.techmentor.bean.entity.ProgresoLeccion;

/** Repositorio JPA de progreso leccion. */
@Repository
public interface ProgresoLeccionRepository extends JpaRepository<ProgresoLeccion, Long> {

    Optional<ProgresoLeccion> findByUsuario_IdUsuarioAndLeccion_IdLeccion(Long idUsuario, Long idLeccion);

    List<ProgresoLeccion> findByUsuario_IdUsuario(Long idUsuario);

    @Query("select p from ProgresoLeccion p where p.usuario.idUsuario = :idUsuario "
            + "and p.leccion.nivel.curso.idCurso = :idCurso")
    List<ProgresoLeccion> listarPorUsuarioYCurso(@Param("idUsuario") Long idUsuario,
                                                 @Param("idCurso") Long idCurso);

    @Query("select count(p) from ProgresoLeccion p where p.usuario.idUsuario = :idUsuario "
            + "and p.estado = :estado and p.leccion.nivel.curso.idCurso = :idCurso")
    long contarPorCurso(@Param("idUsuario") Long idUsuario,
                        @Param("idCurso") Long idCurso,
                        @Param("estado") EstadoProgreso estado);

    @Query("select count(p) from ProgresoLeccion p where p.usuario.idUsuario = :idUsuario "
            + "and p.estado = :estado and p.leccion.nivel.idNivel = :idNivel")
    long contarPorNivel(@Param("idUsuario") Long idUsuario,
                        @Param("idNivel") Long idNivel,
                        @Param("estado") EstadoProgreso estado);
}
