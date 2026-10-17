package com.example.techmentor.persistence;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.techmentor.bean.entity.VerificacionEmail;

/** Repositorio JPA de verificaciones por correo. */
@Repository
public interface VerificacionEmailRepository extends JpaRepository<VerificacionEmail, Long> {
    Optional<VerificacionEmail> findFirstByUsuario_IdUsuarioAndTipoAndUsadoFalseOrderByFechaCreacionDesc(Long idUsuario, String tipo);
}
