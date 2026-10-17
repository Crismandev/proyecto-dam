package com.example.techmentor.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.example.techmentor.bean.dto.*;
import com.example.techmentor.security.*;
import com.example.techmentor.usecase.*;

/** Solo lectura: las respuestas se crean al rendir el examen. */
@RestController
@RequestMapping("/diagnostico")
@RequiredArgsConstructor
public class RespuestaDiagnosticoController {

    private final RespuestaDiagnosticoUseCase respuestaUseCase;

    /** Dueño del resultado, ADMINISTRADOR o MAESTRO. */
    @GetMapping("/resultados/{idResultado}/respuestas")
    public List<RespuestaDiagnosticoDto> listarPorResultado(@PathVariable("idResultado") Long idResultado,
                                                            @AuthenticationPrincipal UsuarioAutenticado usuario) {
        return respuestaUseCase.listarPorResultado(idResultado, usuario);
    }

    @GetMapping("/respuestas/{id}")
    public RespuestaDiagnosticoDto obtener(@PathVariable("id") Long id,
                                           @AuthenticationPrincipal UsuarioAutenticado usuario) {
        return respuestaUseCase.obtener(id, usuario);
    }
}
