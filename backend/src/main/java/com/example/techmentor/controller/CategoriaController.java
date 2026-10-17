package com.example.techmentor.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.example.techmentor.bean.dto.CategoriaDto;
import com.example.techmentor.security.*;
import com.example.techmentor.usecase.CategoriaUseCase;

/** Endpoints REST de categoría (/catalogo/categorias). Los permisos por rol van en cada método. */
@RestController
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaUseCase categoriaUseCase;

    /** Lista solo los activos. */
    @GetMapping("/catalogo/categorias")
    public List<CategoriaDto> listarActivos() {
        return categoriaUseCase.listarActivos();
    }

    /** Lista todos, incluidos los inactivos. */
    @EsStaff
    @GetMapping("/catalogo/categorias/todos")
    public List<CategoriaDto> listarTodos() {
        return categoriaUseCase.listarTodos();
    }

    /** Obtiene uno por id. */
    @GetMapping("/catalogo/categorias/{id}")
    public CategoriaDto obtener(@PathVariable("id") Long id) {
        return categoriaUseCase.obtener(id);
    }

    /** Crea uno nuevo. */
    @EsStaff
    @PostMapping("/catalogo/categorias")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoriaDto crear(@Valid @RequestBody CategoriaDto dto) {
        return categoriaUseCase.crear(dto);
    }

    /** Actualiza uno existente. */
    @EsStaff
    @PutMapping("/catalogo/categorias/{id}")
    public CategoriaDto actualizar(@PathVariable("id") Long id, @Valid @RequestBody CategoriaDto dto) {
        return categoriaUseCase.actualizar(id, dto);
    }

    /** Activa o desactiva sin borrar (?valor=true|false). */
    @EsStaff
    @PatchMapping("/catalogo/categorias/{id}/activo")
    public CategoriaDto cambiarActivo(@PathVariable("id") Long id, @RequestParam("valor") boolean valor) {
        return categoriaUseCase.cambiarActivo(id, valor);
    }

    /** Elimina (409 si tiene datos relacionados; en ese caso usa desactivar). */
    @EsAdmin
    @DeleteMapping("/catalogo/categorias/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable("id") Long id) {
        categoriaUseCase.eliminar(id);
    }
}
