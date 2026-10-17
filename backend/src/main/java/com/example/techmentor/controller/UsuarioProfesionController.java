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

/** Endpoints de las profesiones (puestos) del usuario: la principal define el puesto al que postula. */
@RestController
@RequiredArgsConstructor
public class UsuarioProfesionController {

    private final UsuarioProfesionUseCase usuarioProfesionUseCase;

    /** Mis profesiones. */
    @GetMapping("/usuarios/me/profesiones")
    public List<UsuarioProfesionDto> mias(@AuthenticationPrincipal UsuarioAutenticado usuario) {
        return usuarioProfesionUseCase.listarDeUsuario(usuario.idUsuario());
    }

    /** Me asigno una profesión (la primera queda como principal). */
    @PostMapping("/usuarios/me/profesiones")
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioProfesionDto asignar(@AuthenticationPrincipal UsuarioAutenticado usuario,
                                       @Valid @RequestBody AsignarProfesionDto dto) {
        return usuarioProfesionUseCase.asignar(usuario.idUsuario(), dto);
    }

    /** Marco una de mis profesiones como principal (puesto al que postulo). */
    @PatchMapping("/usuarios/me/profesiones/{idProfesion}/principal")
    public UsuarioProfesionDto marcarPrincipal(@AuthenticationPrincipal UsuarioAutenticado usuario,
                                               @PathVariable("idProfesion") Long idProfesion) {
        return usuarioProfesionUseCase.marcarPrincipal(usuario.idUsuario(), idProfesion);
    }

    /** Quito una de mis profesiones. */
    @DeleteMapping("/usuarios/me/profesiones/{idProfesion}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void quitar(@AuthenticationPrincipal UsuarioAutenticado usuario,
                       @PathVariable("idProfesion") Long idProfesion) {
        usuarioProfesionUseCase.quitar(usuario.idUsuario(), idProfesion);
    }

    /** Profesiones de cualquier usuario. */
    @EsStaff
    @GetMapping("/usuarios/{idUsuario}/profesiones")
    public List<UsuarioProfesionDto> deUsuario(@PathVariable("idUsuario") Long idUsuario) {
        return usuarioProfesionUseCase.listarDeUsuario(idUsuario);
    }
}
