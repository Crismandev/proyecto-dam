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

/** Endpoints para rendir el examen y consultar resultados. */
@RestController
@RequestMapping("/diagnostico")
@RequiredArgsConstructor
public class ResultadoDiagnosticoController {

    private final ResultadoDiagnosticoUseCase resultadoUseCase;

    /** Rendir examen: ALUMNO y USUARIO_VIP. */
    @EsEstudiante
    @PostMapping("/examenes/{idExamen}/rendir")
    @ResponseStatus(HttpStatus.CREATED)
    public ResultadoDiagnosticoDto rendir(@PathVariable("idExamen") Long idExamen,
                                          @Valid @RequestBody RendirExamenDto dto,
                                          @AuthenticationPrincipal UsuarioAutenticado usuario) {
        return resultadoUseCase.rendir(idExamen, dto, usuario);
    }

    /** Resultados del usuario autenticado. */
    @EsEstudiante
    @GetMapping("/resultados/mis-resultados")
    public List<ResultadoDiagnosticoDto> misResultados(@AuthenticationPrincipal UsuarioAutenticado usuario) {
        return resultadoUseCase.misResultados(usuario);
    }

    /** Dueño del resultado, ADMINISTRADOR o MAESTRO. */
    @GetMapping("/resultados/{id}")
    public ResultadoDiagnosticoDto obtener(@PathVariable("id") Long id,
                                           @AuthenticationPrincipal UsuarioAutenticado usuario) {
        return resultadoUseCase.obtener(id, usuario);
    }

    @EsStaff
    @GetMapping("/resultados/usuario/{idUsuario}")
    public List<ResultadoDiagnosticoDto> listarPorUsuario(@PathVariable("idUsuario") Long idUsuario) {
        return resultadoUseCase.listarPorUsuario(idUsuario);
    }

    @EsStaff
    @GetMapping("/resultados/examen/{idExamen}")
    public List<ResultadoDiagnosticoDto> listarPorExamen(@PathVariable("idExamen") Long idExamen) {
        return resultadoUseCase.listarPorExamen(idExamen);
    }

    /** Cambio manual del nivel asignado. */
    @EsStaff
    @PutMapping("/resultados/{id}/nivel")
    public ResultadoDiagnosticoDto cambiarNivel(@PathVariable("id") Long id,
                                                @Valid @RequestBody ResultadoNivelDto dto) {
        return resultadoUseCase.cambiarNivel(id, dto.getIdNivelAsignado());
    }

    @EsAdmin
    @DeleteMapping("/resultados/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable("id") Long id) {
        resultadoUseCase.eliminar(id);
    }
}
