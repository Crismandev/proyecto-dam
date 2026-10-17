package com.example.techmentor.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.example.techmentor.bean.dto.*;
import com.example.techmentor.security.*;
import com.example.techmentor.usecase.*;

/** Gestión de opciones (incluye es_correcta): solo ADMINISTRADOR y MAESTRO. */
@EsStaff
@RestController
@RequestMapping("/diagnostico")
@RequiredArgsConstructor
public class OpcionDiagnosticoController {

    private final OpcionDiagnosticoUseCase opcionUseCase;

    @GetMapping("/preguntas/{idPregunta}/opciones")
    public List<OpcionDiagnosticoDto> listarPorPregunta(@PathVariable("idPregunta") Long idPregunta) {
        return opcionUseCase.listarPorPregunta(idPregunta);
    }

    @GetMapping("/opciones/{id}")
    public OpcionDiagnosticoDto obtener(@PathVariable("id") Long id) {
        return opcionUseCase.obtener(id);
    }

    @PostMapping("/opciones")
    @ResponseStatus(HttpStatus.CREATED)
    public OpcionDiagnosticoDto crear(@Valid @RequestBody OpcionDiagnosticoDto dto) {
        return opcionUseCase.crear(dto);
    }

    @PutMapping("/opciones/{id}")
    public OpcionDiagnosticoDto actualizar(@PathVariable("id") Long id,
                                           @Valid @RequestBody OpcionDiagnosticoDto dto) {
        return opcionUseCase.actualizar(id, dto);
    }

    @DeleteMapping("/opciones/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable("id") Long id) {
        opcionUseCase.eliminar(id);
    }
}
