package com.example.techmentor.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.example.techmentor.bean.dto.CursoDto;
import com.example.techmentor.security.*;
import com.example.techmentor.usecase.CursoUseCase;

/** Endpoints REST de curso (/catalogo/cursos). Los permisos por rol van en cada método. */
@RestController
@RequiredArgsConstructor
public class CursoController {

    private final CursoUseCase cursoUseCase;

    /** Lista los activos (filtro opcional ?idCategoria=). */
    @GetMapping("/catalogo/cursos")
    public List<CursoDto> listarActivos(@RequestParam(name = "idCategoria", required = false) Long idCategoria) {
        return cursoUseCase.listarActivos(idCategoria);
    }

    /** Lista todos, incluidos los inactivos. */
    @EsStaff
    @GetMapping("/catalogo/cursos/todos")
    public List<CursoDto> listarTodos() {
        return cursoUseCase.listarTodos();
    }

    /** Obtiene uno por id. */
    @GetMapping("/catalogo/cursos/{id}")
    public CursoDto obtener(@PathVariable("id") Long id) {
        return cursoUseCase.obtener(id);
    }

    /** Crea uno nuevo. */
    @EsStaff
    @PostMapping("/catalogo/cursos")
    @ResponseStatus(HttpStatus.CREATED)
    public CursoDto crear(@Valid @RequestBody CursoDto dto) {
        return cursoUseCase.crear(dto);
    }

    /** Actualiza uno existente. */
    @EsStaff
    @PutMapping("/catalogo/cursos/{id}")
    public CursoDto actualizar(@PathVariable("id") Long id, @Valid @RequestBody CursoDto dto) {
        return cursoUseCase.actualizar(id, dto);
    }

    /** Activa o desactiva sin borrar (?valor=true|false). */
    @EsStaff
    @PatchMapping("/catalogo/cursos/{id}/activo")
    public CursoDto cambiarActivo(@PathVariable("id") Long id, @RequestParam("valor") boolean valor) {
        return cursoUseCase.cambiarActivo(id, valor);
    }

    /** Elimina (409 si tiene datos relacionados; en ese caso usa desactivar). */
    @EsAdmin
    @DeleteMapping("/catalogo/cursos/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable("id") Long id) {
        cursoUseCase.eliminar(id);
    }
}
