package com.example.techmentor.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.example.techmentor.bean.dto.*;
import com.example.techmentor.security.*;
import com.example.techmentor.usecase.*;

/** Endpoints de las misiones del usuario (/gamificacion). */
@RestController
@RequiredArgsConstructor
public class MisionUsuarioController {

    private final MisionUsuarioUseCase misionUsuarioUseCase;

    /** Me uno a una misión activa. */
    @EsEstudiante
    @PostMapping("/gamificacion/misiones/{idMision}/unirse")
    @ResponseStatus(HttpStatus.CREATED)
    public MisionUsuarioDto unirse(@PathVariable("idMision") Long idMision,
                                   @AuthenticationPrincipal UsuarioAutenticado usuario) {
        return misionUsuarioUseCase.unirse(idMision, usuario.idUsuario());
    }

    /** Mis misiones y su avance. */
    @EsEstudiante
    @GetMapping("/gamificacion/mis-misiones")
    public List<MisionUsuarioDto> misMisiones(@AuthenticationPrincipal UsuarioAutenticado usuario) {
        return misionUsuarioUseCase.misMisiones(usuario.idUsuario());
    }

    /** Misiones de cualquier usuario. */
    @EsStaff
    @GetMapping("/gamificacion/usuarios/{idUsuario}/misiones")
    public List<MisionUsuarioDto> deUsuario(@PathVariable("idUsuario") Long idUsuario) {
        return misionUsuarioUseCase.listarPorUsuario(idUsuario);
    }

    /** Fija el progreso de una misión; al llegar a la meta se completa y da XP. */
    @EsStaff
    @PutMapping("/gamificacion/misiones-usuario/{id}/progreso")
    public MisionUsuarioDto actualizarProgreso(@PathVariable("id") Long id,
                                               @Valid @RequestBody ProgresoMisionDto dto) {
        return misionUsuarioUseCase.actualizarProgreso(id, dto);
    }
}
