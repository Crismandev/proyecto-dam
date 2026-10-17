package com.example.techmentor.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.example.techmentor.bean.dto.CompletarLeccionDto;
import com.example.techmentor.bean.dto.ProgresoLeccionDto;
import com.example.techmentor.security.EsAdmin;
import com.example.techmentor.security.EsEstudiante;
import com.example.techmentor.security.EsStaff;
import com.example.techmentor.security.UsuarioAutenticado;
import com.example.techmentor.usecase.ProgresoLeccionUseCase;

/** Endpoints REST de progreso leccion. */
@RestController
@RequestMapping("/progreso/lecciones")
@RequiredArgsConstructor
public class ProgresoLeccionController {

    private final ProgresoLeccionUseCase progresoLeccionUseCase;

    @EsEstudiante
    @PostMapping("/{idLeccion}/iniciar")
    public ProgresoLeccionDto iniciar(@PathVariable("idLeccion") Long idLeccion,
                                      @AuthenticationPrincipal UsuarioAutenticado usuario) {
        return progresoLeccionUseCase.iniciar(idLeccion, usuario);
    }

    @EsEstudiante
    @PostMapping("/{idLeccion}/completar")
    public ProgresoLeccionDto completar(@PathVariable("idLeccion") Long idLeccion,
                                        @Valid @RequestBody CompletarLeccionDto dto,
                                        @AuthenticationPrincipal UsuarioAutenticado usuario) {
        return progresoLeccionUseCase.completar(idLeccion, dto, usuario);
    }

    /** Opcional ?idCurso= para filtrar por curso. */
    @EsEstudiante
    @GetMapping("/mis-lecciones")
    public List<ProgresoLeccionDto> misLecciones(
            @RequestParam(name = "idCurso", required = false) Long idCurso,
            @AuthenticationPrincipal UsuarioAutenticado usuario) {
        return progresoLeccionUseCase.misLecciones(idCurso, usuario);
    }

    /** Dueño del progreso, ADMINISTRADOR o MAESTRO. */
    @GetMapping("/{id}")
    public ProgresoLeccionDto obtener(@PathVariable("id") Long id,
                                      @AuthenticationPrincipal UsuarioAutenticado usuario) {
        return progresoLeccionUseCase.obtener(id, usuario);
    }

    @EsStaff
    @GetMapping("/usuario/{idUsuario}")
    public List<ProgresoLeccionDto> listarPorUsuario(@PathVariable("idUsuario") Long idUsuario) {
        return progresoLeccionUseCase.listarPorUsuario(idUsuario);
    }

    @EsAdmin
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable("id") Long id) {
        progresoLeccionUseCase.eliminar(id);
    }
}
