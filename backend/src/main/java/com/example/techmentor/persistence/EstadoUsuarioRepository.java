package com.example.techmentor.persistence;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.techmentor.bean.entity.EstadoUsuario;

/** Repositorio JPA de estado de usuario: consultas a la tabla `estados_usuario`. */
@Repository
public interface EstadoUsuarioRepository extends JpaRepository<EstadoUsuario, Long> {

    Optional<EstadoUsuario> findByNombre(String nombre);
}
