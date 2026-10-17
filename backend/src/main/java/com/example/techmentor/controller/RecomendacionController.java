package com.example.techmentor.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.example.techmentor.bean.dto.*;
import com.example.techmentor.security.*;
import com.example.techmentor.usecase.*;

/** Endpoints de recomendación de cursos según el diagnóstico y el puesto (/recomendaciones). */
@RestController
@RequiredArgsConstructor
public class RecomendacionController {

    private final RecomendacionUseCase recomendacionUseCase;

    /** Genera las recomendaciones de un resultado (dueño del resultado o staff). */
    @PostMapping("/recomendaciones/generar/{idResultado}")
    public List<RecomendacionCursoDto> generar(@PathVariable("idResultado") Long idResultado,
                                               @AuthenticationPrincipal UsuarioAutenticado usuario) {
        return recomendacionUseCase.generar(idResultado, usuario);
    }

    /** Mis recomendaciones. */
    @EsEstudiante
    @GetMapping("/recomendaciones/mis-recomendaciones")
    public List<RecomendacionCursoDto> mias(@AuthenticationPrincipal UsuarioAutenticado usuario) {
        return recomendacionUseCase.misRecomendaciones(usuario.idUsuario());
    }

    /** Recomendaciones de un resultado. */
    @GetMapping("/recomendaciones/resultado/{idResultado}")
    public List<RecomendacionCursoDto> porResultado(@PathVariable("idResultado") Long idResultado,
                                                    @AuthenticationPrincipal UsuarioAutenticado usuario) {
        return recomendacionUseCase.porResultado(idResultado, usuario);
    }

    /** Porcentaje por categoría de pregunta de un resultado. */
    @GetMapping("/recomendaciones/resultado/{idResultado}/analisis")
    public List<AnalisisCategoriaDto> analisis(@PathVariable("idResultado") Long idResultado,
                                               @AuthenticationPrincipal UsuarioAutenticado usuario) {
        return recomendacionUseCase.analisis(idResultado, usuario);
    }

    /** Recomendaciones de cualquier usuario. */
    @EsStaff
    @GetMapping("/recomendaciones/usuario/{idUsuario}")
    public List<RecomendacionCursoDto> deUsuario(@PathVariable("idUsuario") Long idUsuario) {
        return recomendacionUseCase.listarPorUsuario(idUsuario);
    }

    /** Cambia el estado (PENDIENTE, INICIADA, DESCARTADA): ?valor=INICIADA. */
    @PatchMapping("/recomendaciones/{id}/estado")
    public RecomendacionCursoDto cambiarEstado(@PathVariable("id") Long id, @RequestParam("valor") String valor,
                                               @AuthenticationPrincipal UsuarioAutenticado usuario) {
        return recomendacionUseCase.cambiarEstado(id, valor, usuario);
    }
}
