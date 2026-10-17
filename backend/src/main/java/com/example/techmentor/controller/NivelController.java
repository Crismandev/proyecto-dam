package com.example.techmentor.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.example.techmentor.bean.dto.NivelDto;
import com.example.techmentor.security.*;
import com.example.techmentor.usecase.NivelUseCase;

/** Endpoints REST de nivel (/catalogo/niveles). Los permisos por rol van en cada método. */
@RestController
@RequiredArgsConstructor
public class NivelController {

    private final NivelUseCase nivelUseCase;

    /** Lista todos los registros. */
    @GetMapping("/catalogo/niveles")
    public List<NivelDto> listar() {
        return nivelUseCase.listar();
    }

    /** Lista los registros de un(a) curso. */
    @GetMapping("/catalogo/cursos/{idCurso}/niveles")
    public List<NivelDto> listarPorCurso(@PathVariable("idCurso") Long idCurso) {
        return nivelUseCase.listarPorCurso(idCurso);
    }

    /** Obtiene uno por id. */
    @GetMapping("/catalogo/niveles/{id}")
    public NivelDto obtener(@PathVariable("id") Long id) {
        return nivelUseCase.obtener(id);
    }

    /** Crea uno nuevo. */
    @EsStaff
    @PostMapping("/catalogo/niveles")
    @ResponseStatus(HttpStatus.CREATED)
    public NivelDto crear(@Valid @RequestBody NivelDto dto) {
        return nivelUseCase.crear(dto);
    }

    /** Actualiza uno existente. */
    @EsStaff
    @PutMapping("/catalogo/niveles/{id}")
    public NivelDto actualizar(@PathVariable("id") Long id, @Valid @RequestBody NivelDto dto) {
        return nivelUseCase.actualizar(id, dto);
    }

    /** Elimina (409 si tiene datos relacionados; en ese caso usa desactivar). */
    @EsStaff
    @DeleteMapping("/catalogo/niveles/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable("id") Long id) {
        nivelUseCase.eliminar(id);
    }
}
