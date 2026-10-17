package com.example.techmentor.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.example.techmentor.bean.dto.*;
import com.example.techmentor.security.*;
import com.example.techmentor.usecase.*;

/** Endpoints REST de categoria pregunta. */
@RestController
@RequestMapping("/diagnostico/categorias-pregunta")
@RequiredArgsConstructor
public class CategoriaPreguntaController {

    private final CategoriaPreguntaUseCase categoriaPreguntaUseCase;

    /** Cualquier usuario autenticado. */
    @GetMapping
    public List<CategoriaPreguntaDto> listar() {
        return categoriaPreguntaUseCase.listar();
    }

    /** Cualquier usuario autenticado. */
    @GetMapping("/{id}")
    public CategoriaPreguntaDto obtener(@PathVariable("id") Long id) {
        return categoriaPreguntaUseCase.obtener(id);
    }

    @EsStaff
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoriaPreguntaDto crear(@Valid @RequestBody CategoriaPreguntaDto dto) {
        return categoriaPreguntaUseCase.crear(dto);
    }

    @EsStaff
    @PutMapping("/{id}")
    public CategoriaPreguntaDto actualizar(@PathVariable("id") Long id,
                                           @Valid @RequestBody CategoriaPreguntaDto dto) {
        return categoriaPreguntaUseCase.actualizar(id, dto);
    }

    @EsStaff
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable("id") Long id) {
        categoriaPreguntaUseCase.eliminar(id);
    }
}
