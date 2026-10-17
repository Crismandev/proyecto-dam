package com.example.techmentor.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.example.techmentor.bean.dto.*;
import com.example.techmentor.security.*;
import com.example.techmentor.usecase.*;

/** Endpoint para abrir un curso: niveles, lecciones, videos/textos, ejercicios y avance del alumno. */
@RestController
@RequiredArgsConstructor
public class ContenidoCursoController {

    private final ContenidoCursoUseCase contenidoCursoUseCase;

    /** Contenido completo del curso (sin respuestas correctas). Cualquier usuario autenticado. */
    @GetMapping("/catalogo/cursos/{idCurso}/contenido")
    public ContenidoCursoDto obtener(@PathVariable("idCurso") Long idCurso,
                                     @AuthenticationPrincipal UsuarioAutenticado usuario) {
        return contenidoCursoUseCase.obtener(idCurso, usuario);
    }
}
