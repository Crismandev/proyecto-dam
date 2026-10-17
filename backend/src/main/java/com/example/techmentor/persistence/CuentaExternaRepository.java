package com.example.techmentor.persistence;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.techmentor.bean.entity.CuentaExterna;

/** Repositorio JPA de cuentas externas (Google/GitHub). */
@Repository
public interface CuentaExternaRepository extends JpaRepository<CuentaExterna, Long> {
    Optional<CuentaExterna> findByProveedorAndIdExterno(String proveedor, String idExterno);
    List<CuentaExterna> findByUsuario_IdUsuario(Long idUsuario);
}
