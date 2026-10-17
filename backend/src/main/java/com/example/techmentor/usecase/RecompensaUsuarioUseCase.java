package com.example.techmentor.usecase;

import java.util.List;
import com.example.techmentor.bean.dto.RecompensaUsuarioDto;

/** Casos de uso de las recompensas de un usuario. */
public interface RecompensaUsuarioUseCase {

    /** Obtiene una recompensa (descuenta monedas si cuesta; valida la racha si la exige). */
    RecompensaUsuarioDto obtener(Long idRecompensa, Long idUsuario);

    /** Recompensas del usuario autenticado. */
    List<RecompensaUsuarioDto> misRecompensas(Long idUsuario);

    /** Recompensas de cualquier usuario (staff). */
    List<RecompensaUsuarioDto> listarPorUsuario(Long idUsuario);

    /** Reclama la recompensa: suma el XP o las monedas al usuario. */
    RecompensaUsuarioDto reclamar(Long idRecompensaUsuario, Long idUsuario);
}
