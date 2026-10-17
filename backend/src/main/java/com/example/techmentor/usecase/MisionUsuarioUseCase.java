package com.example.techmentor.usecase;

import java.util.List;
import com.example.techmentor.bean.dto.MisionUsuarioDto;
import com.example.techmentor.bean.dto.ProgresoMisionDto;

/** Casos de uso de las misiones de un usuario. */
public interface MisionUsuarioUseCase {

    /** El usuario se une a una misión activa y vigente. */
    MisionUsuarioDto unirse(Long idMision, Long idUsuario);

    /** Misiones del usuario autenticado. */
    List<MisionUsuarioDto> misMisiones(Long idUsuario);

    /** Misiones de cualquier usuario (staff). */
    List<MisionUsuarioDto> listarPorUsuario(Long idUsuario);

    /** Fija el progreso; al llegar a la meta se completa y se otorga el XP de la misión. */
    MisionUsuarioDto actualizarProgreso(Long idMisionUsuario, ProgresoMisionDto dto);
}
