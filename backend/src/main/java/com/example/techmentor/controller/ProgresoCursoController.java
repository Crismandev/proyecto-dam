package com.example.techmentor.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.example.techmentor.bean.dto.ProgresoCursoDto;
import com.example.techmentor.security.EsAdmin;
import com.example.techmentor.security.EsEstudiante;
import com.example.techmentor.security.EsStaff;
import com.example.techmentor.security.UsuarioAutenticado;
import com.example.techmentor.usecase.ProgresoCursoUseCase;

/** Endpoints REST de progreso curso. */
@RestController
@RequestMapping("/progreso/cursos")
@RequiredArgsConstructor
public class ProgresoCursoController {

    private final ProgresoCursoUseCase progresoCursoUseCase;

    /** Inicia (o reanuda) un curso. ALUMNO y USUARIO_VIP. */
    @EsEstudiante
    @PostMapping("/{idCurso}/iniciar")
    public ProgresoCursoDto iniciar(@PathVariable("idCurso") Long idCurso,
                                    @AuthenticationPrincipal UsuarioAutenticado usuario) {
        return progresoCursoUseCase.iniciar(idCurso, usuario);
    }

    @EsEstudiante
    @PutMapping("/{idCurso}/abandonar")
    public ProgresoCursoDto abandonar(@PathVariable("idCurso") Long idCurso,
                                      @AuthenticationPrincipal UsuarioAutenticado usuario) {
        return progresoCursoUseCase.abandonar(idCurso, usuario);
    }

    @EsEstudiante
    @GetMapping("/mis-cursos")
    public List<ProgresoCursoDto> misCursos(@AuthenticationPrincipal UsuarioAutenticado usuario) {
        return progresoCursoUseCase.misCursos(usuario);
    }

    @EsEstudiante
    @GetMapping("/mis-cursos/{idCurso}")
    public ProgresoCursoDto miProgreso(@PathVariable("idCurso") Long idCurso,
                                       @AuthenticationPrincipal UsuarioAutenticado usuario) {
        return progresoCursoUseCase.miProgreso(idCurso, usuario);
    }

    /** Dueño del progreso, ADMINISTRADOR o MAESTRO. */
    @GetMapping("/{id}")
    public ProgresoCursoDto obtener(@PathVariable("id") Long id,
                                    @AuthenticationPrincipal UsuarioAutenticado usuario) {
        return progresoCursoUseCase.obtener(id, usuario);
    }

    @EsStaff
    @GetMapping("/usuario/{idUsuario}")
    public List<ProgresoCursoDto> listarPorUsuario(@PathVariable("idUsuario") Long idUsuario) {
        return progresoCursoUseCase.listarPorUsuario(idUsuario);
    }

    @EsStaff
    @GetMapping("/curso/{idCurso}")
    public List<ProgresoCursoDto> listarPorCurso(@PathVariable("idCurso") Long idCurso) {
        return progresoCursoUseCase.listarPorCurso(idCurso);
    }

    @EsAdmin
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable("id") Long id) {
        progresoCursoUseCase.eliminar(id);
    }
}
