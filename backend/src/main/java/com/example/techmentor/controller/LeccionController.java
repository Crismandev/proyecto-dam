package com.example.techmentor.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.example.techmentor.bean.dto.LeccionDto;
import com.example.techmentor.security.*;
import com.example.techmentor.usecase.LeccionUseCase;

/** Endpoints REST de lección (/catalogo/lecciones). Los permisos por rol van en cada método. */
@RestController
@RequiredArgsConstructor
public class LeccionController {

    private final LeccionUseCase leccionUseCase;

    /** Lista todos los registros. */
    @GetMapping("/catalogo/lecciones")
    public List<LeccionDto> listar() {
        return leccionUseCase.listar();
    }

    /** Lista los registros de un(a) nivel. */
    @GetMapping("/catalogo/niveles/{idNivel}/lecciones")
    public List<LeccionDto> listarPorNivel(@PathVariable("idNivel") Long idNivel) {
        return leccionUseCase.listarPorNivel(idNivel);
    }

    /** Obtiene uno por id. */
    @GetMapping("/catalogo/lecciones/{id}")
    public LeccionDto obtener(@PathVariable("id") Long id) {
        return leccionUseCase.obtener(id);
    }

    /** Crea uno nuevo. */
    @EsStaff
    @PostMapping("/catalogo/lecciones")
    @ResponseStatus(HttpStatus.CREATED)
    public LeccionDto crear(@Valid @RequestBody LeccionDto dto) {
        return leccionUseCase.crear(dto);
    }

    /** Actualiza uno existente. */
    @EsStaff
    @PutMapping("/catalogo/lecciones/{id}")
    public LeccionDto actualizar(@PathVariable("id") Long id, @Valid @RequestBody LeccionDto dto) {
        return leccionUseCase.actualizar(id, dto);
    }

    /** Elimina (409 si tiene datos relacionados; en ese caso usa desactivar). */
    @EsStaff
    @DeleteMapping("/catalogo/lecciones/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable("id") Long id) {
        leccionUseCase.eliminar(id);
    }
}
