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

/** Endpoints de usuarios (/usuarios): consulta, perfil propio, alta, estado, rol, baja e historial. */
@RestController
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioUseCase usuarioUseCase;

    /** Lista todos los usuarios. */
    @EsStaff
    @GetMapping("/usuarios")
    public List<UsuarioDto> listar() {
        return usuarioUseCase.listar();
    }

    /** Perfil del usuario autenticado. */
    @GetMapping("/usuarios/me")
    public UsuarioDto miPerfil(@AuthenticationPrincipal UsuarioAutenticado usuario) {
        return usuarioUseCase.miPerfil(usuario);
    }

    /** Edita el perfil propio (los campos null no se modifican). */
    @PutMapping("/usuarios/me")
    public UsuarioDto actualizarMiPerfil(@AuthenticationPrincipal UsuarioAutenticado usuario,
                                         @Valid @RequestBody UsuarioPerfilDto dto) {
        return usuarioUseCase.actualizarMiPerfil(usuario, dto);
    }

    /** Un usuario: solo él mismo o ADMINISTRADOR/MAESTRO. */
    @GetMapping("/usuarios/{id}")
    public UsuarioDto obtener(@PathVariable("id") Long id, @AuthenticationPrincipal UsuarioAutenticado solicitante) {
        return usuarioUseCase.obtener(id, solicitante);
    }

    /** Crea un usuario con su credencial. */
    @EsAdmin
    @PostMapping("/usuarios")
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioDto crear(@Valid @RequestBody UsuarioCrearDto dto) {
        return usuarioUseCase.crear(dto);
    }

    /** Edita el perfil de cualquier usuario. */
    @EsAdmin
    @PutMapping("/usuarios/{id}")
    public UsuarioDto actualizarPerfil(@PathVariable("id") Long id, @Valid @RequestBody UsuarioPerfilDto dto) {
        return usuarioUseCase.actualizarPerfil(id, dto);
    }

    /** Cambia el estado (desactivar, bloquear, reactivar) y lo registra en el historial. */
    @EsAdmin
    @PatchMapping("/usuarios/{id}/estado")
    public UsuarioDto cambiarEstado(@PathVariable("id") Long id, @Valid @RequestBody CambiarEstadoDto dto,
                                    @AuthenticationPrincipal UsuarioAutenticado admin) {
        return usuarioUseCase.cambiarEstado(id, dto, admin);
    }

    /** Cambia el rol de un usuario. */
    @EsAdmin
    @PatchMapping("/usuarios/{id}/rol")
    public UsuarioDto cambiarRol(@PathVariable("id") Long id, @Valid @RequestBody CambiarRolDto dto,
                                 @AuthenticationPrincipal UsuarioAutenticado admin) {
        return usuarioUseCase.cambiarRol(id, dto, admin);
    }

    /** Elimina un usuario y sus datos (prefiere cambiar el estado). */
    @EsAdmin
    @DeleteMapping("/usuarios/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable("id") Long id, @AuthenticationPrincipal UsuarioAutenticado admin) {
        usuarioUseCase.eliminar(id, admin);
    }

    /** Historial de cambios de estado. */
    @EsStaff
    @GetMapping("/usuarios/{id}/historial-estados")
    public List<HistorialEstadoDto> historial(@PathVariable("id") Long id) {
        return usuarioUseCase.historial(id);
    }
}
