package com.example.techmentor.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.example.techmentor.bean.dto.*;
import com.example.techmentor.security.*;
import com.example.techmentor.usecase.*;

/** Gestión de preguntas: solo ADMINISTRADOR y MAESTRO. */
@EsStaff
@RestController
@RequestMapping("/diagnostico")
@RequiredArgsConstructor
public class PreguntaDiagnosticoController {

    private final PreguntaDiagnosticoUseCase preguntaUseCase;

    @GetMapping("/examenes/{idExamen}/preguntas")
    public List<PreguntaDiagnosticoDto> listarPorExamen(@PathVariable("idExamen") Long idExamen) {
        return preguntaUseCase.listarPorExamen(idExamen);
    }

    @GetMapping("/preguntas/{id}")
    public PreguntaDiagnosticoDto obtener(@PathVariable("id") Long id) {
        return preguntaUseCase.obtener(id);
    }

    @PostMapping("/preguntas")
    @ResponseStatus(HttpStatus.CREATED)
    public PreguntaDiagnosticoDto crear(@Valid @RequestBody PreguntaDiagnosticoDto dto) {
        return preguntaUseCase.crear(dto);
    }

    @PutMapping("/preguntas/{id}")
    public PreguntaDiagnosticoDto actualizar(@PathVariable("id") Long id,
                                             @Valid @RequestBody PreguntaDiagnosticoDto dto) {
        return preguntaUseCase.actualizar(id, dto);
    }

    @DeleteMapping("/preguntas/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable("id") Long id) {
        preguntaUseCase.eliminar(id);
    }
}
