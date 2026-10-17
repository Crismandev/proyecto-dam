package com.example.techmentor.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.example.techmentor.bean.dto.RecursoLeccionDto;
import com.example.techmentor.security.*;
import com.example.techmentor.usecase.RecursoLeccionUseCase;

/** Endpoints REST de recurso de lección (/catalogo/recursos). Los permisos por rol van en cada método. */
@RestController
@RequiredArgsConstructor
public class RecursoLeccionController {

    private final RecursoLeccionUseCase recursoLeccionUseCase;

    /** Lista todos los registros. */
    @EsStaff
    @GetMapping("/catalogo/recursos")
    public List<RecursoLeccionDto> listar() {
        return recursoLeccionUseCase.listar();
    }

    /** Lista los registros de un(a) leccion. */
    @EsStaff
    @GetMapping("/catalogo/lecciones/{idLeccion}/recursos")
    public List<RecursoLeccionDto> listarPorLeccion(@PathVariable("idLeccion") Long idLeccion) {
        return recursoLeccionUseCase.listarPorLeccion(idLeccion);
    }

    /** Obtiene uno por id. */
    @EsStaff
    @GetMapping("/catalogo/recursos/{id}")
    public RecursoLeccionDto obtener(@PathVariable("id") Long id) {
        return recursoLeccionUseCase.obtener(id);
    }

    /** Crea uno nuevo. */
    @EsStaff
    @PostMapping("/catalogo/recursos")
    @ResponseStatus(HttpStatus.CREATED)
    public RecursoLeccionDto crear(@Valid @RequestBody RecursoLeccionDto dto) {
        return recursoLeccionUseCase.crear(dto);
    }

    /** Actualiza uno existente. */
    @EsStaff
    @PutMapping("/catalogo/recursos/{id}")
    public RecursoLeccionDto actualizar(@PathVariable("id") Long id, @Valid @RequestBody RecursoLeccionDto dto) {
        return recursoLeccionUseCase.actualizar(id, dto);
    }

    /** Activa o desactiva sin borrar (?valor=true|false). */
    @EsStaff
    @PatchMapping("/catalogo/recursos/{id}/activo")
    public RecursoLeccionDto cambiarActivo(@PathVariable("id") Long id, @RequestParam("valor") boolean valor) {
        return recursoLeccionUseCase.cambiarActivo(id, valor);
    }

    /** Elimina (409 si tiene datos relacionados; en ese caso usa desactivar). */
    @EsStaff
    @DeleteMapping("/catalogo/recursos/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable("id") Long id) {
        recursoLeccionUseCase.eliminar(id);
    }
}
