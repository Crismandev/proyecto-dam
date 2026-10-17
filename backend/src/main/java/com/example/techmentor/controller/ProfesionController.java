package com.example.techmentor.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.example.techmentor.bean.dto.ProfesionDto;
import com.example.techmentor.security.*;
import com.example.techmentor.usecase.ProfesionUseCase;

/** Endpoints REST de profesión (/catalogo/profesiones). Los permisos por rol van en cada método. */
@RestController
@RequiredArgsConstructor
public class ProfesionController {

    private final ProfesionUseCase profesionUseCase;

    /** Lista solo los activos. */
    @GetMapping("/catalogo/profesiones")
    public List<ProfesionDto> listarActivos() {
        return profesionUseCase.listarActivos();
    }

    /** Lista todos, incluidos los inactivos. */
    @EsStaff
    @GetMapping("/catalogo/profesiones/todos")
    public List<ProfesionDto> listarTodos() {
        return profesionUseCase.listarTodos();
    }

    /** Obtiene uno por id. */
    @GetMapping("/catalogo/profesiones/{id}")
    public ProfesionDto obtener(@PathVariable("id") Long id) {
        return profesionUseCase.obtener(id);
    }

    /** Crea uno nuevo. */
    @EsStaff
    @PostMapping("/catalogo/profesiones")
    @ResponseStatus(HttpStatus.CREATED)
    public ProfesionDto crear(@Valid @RequestBody ProfesionDto dto) {
        return profesionUseCase.crear(dto);
    }

    /** Actualiza uno existente. */
    @EsStaff
    @PutMapping("/catalogo/profesiones/{id}")
    public ProfesionDto actualizar(@PathVariable("id") Long id, @Valid @RequestBody ProfesionDto dto) {
        return profesionUseCase.actualizar(id, dto);
    }

    /** Activa o desactiva sin borrar (?valor=true|false). */
    @EsStaff
    @PatchMapping("/catalogo/profesiones/{id}/activo")
    public ProfesionDto cambiarActivo(@PathVariable("id") Long id, @RequestParam("valor") boolean valor) {
        return profesionUseCase.cambiarActivo(id, valor);
    }

    /** Elimina (409 si tiene datos relacionados; en ese caso usa desactivar). */
    @EsAdmin
    @DeleteMapping("/catalogo/profesiones/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable("id") Long id) {
        profesionUseCase.eliminar(id);
    }
}
