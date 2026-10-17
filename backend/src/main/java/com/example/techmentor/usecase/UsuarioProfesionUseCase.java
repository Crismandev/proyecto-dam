package com.example.techmentor.usecase;

import java.util.List;
import com.example.techmentor.bean.dto.AsignarProfesionDto;
import com.example.techmentor.bean.dto.UsuarioProfesionDto;

/** Casos de uso de las profesiones (puestos) de un usuario; una sola es la principal. */
public interface UsuarioProfesionUseCase {

    /** Lista las profesiones de un usuario. */
    List<UsuarioProfesionDto> listarDeUsuario(Long idUsuario);

    /** Asigna una profesión al usuario (la primera queda como principal). */
    UsuarioProfesionDto asignar(Long idUsuario, AsignarProfesionDto dto);

    /** Marca una de sus profesiones como principal (puesto al que postula). */
    UsuarioProfesionDto marcarPrincipal(Long idUsuario, Long idProfesion);

    /** Quita una profesión del usuario. */
    void quitar(Long idUsuario, Long idProfesion);
}
