package com.example.techmentor.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.example.techmentor.bean.dto.*;
import com.example.techmentor.security.*;
import com.example.techmentor.usecase.*;

/** Endpoints de las recompensas del usuario (/gamificacion). */
@RestController
@RequiredArgsConstructor
public class RecompensaUsuarioController {

    private final RecompensaUsuarioUseCase recompensaUsuarioUseCase;

    /** Obtengo una recompensa (si cuesta monedas se descuentan; si exige racha se valida). */
    @EsEstudiante
    @PostMapping("/gamificacion/recompensas/{idRecompensa}/obtener")
    @ResponseStatus(HttpStatus.CREATED)
    public RecompensaUsuarioDto obtener(@PathVariable("idRecompensa") Long idRecompensa,
                                        @AuthenticationPrincipal UsuarioAutenticado usuario) {
        return recompensaUsuarioUseCase.obtener(idRecompensa, usuario.idUsuario());
    }

    /** Mis recompensas. */
    @EsEstudiante
    @GetMapping("/gamificacion/mis-recompensas")
    public List<RecompensaUsuarioDto> misRecompensas(@AuthenticationPrincipal UsuarioAutenticado usuario) {
        return recompensaUsuarioUseCase.misRecompensas(usuario.idUsuario());
    }

    /** Reclamo una recompensa obtenida: suma el XP o las monedas. */
    @EsEstudiante
    @PatchMapping("/gamificacion/mis-recompensas/{id}/reclamar")
    public RecompensaUsuarioDto reclamar(@PathVariable("id") Long id,
                                         @AuthenticationPrincipal UsuarioAutenticado usuario) {
        return recompensaUsuarioUseCase.reclamar(id, usuario.idUsuario());
    }

    /** Recompensas de cualquier usuario. */
    @EsStaff
    @GetMapping("/gamificacion/usuarios/{idUsuario}/recompensas")
    public List<RecompensaUsuarioDto> deUsuario(@PathVariable("idUsuario") Long idUsuario) {
        return recompensaUsuarioUseCase.listarPorUsuario(idUsuario);
    }
}
