package com.example.techmentor.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.example.techmentor.bean.dto.MisionDto;
import com.example.techmentor.security.*;
import com.example.techmentor.usecase.MisionUseCase;

/** Endpoints REST de misión (/gamificacion/misiones). Los permisos por rol van en cada método. */
@RestController
@RequiredArgsConstructor
public class MisionController {

    private final MisionUseCase misionUseCase;

    /** Lista solo los activos. */
    @GetMapping("/gamificacion/misiones")
    public List<MisionDto> listarActivos() {
        return misionUseCase.listarActivos();
    }

    /** Lista todos, incluidos los inactivos. */
    @EsStaff
    @GetMapping("/gamificacion/misiones/todos")
    public List<MisionDto> listarTodos() {
        return misionUseCase.listarTodos();
    }

    /** Obtiene uno por id. */
    @GetMapping("/gamificacion/misiones/{id}")
    public MisionDto obtener(@PathVariable("id") Long id) {
        return misionUseCase.obtener(id);
    }

    /** Crea uno nuevo. */
    @EsStaff
    @PostMapping("/gamificacion/misiones")
    @ResponseStatus(HttpStatus.CREATED)
    public MisionDto crear(@Valid @RequestBody MisionDto dto) {
        return misionUseCase.crear(dto);
    }

    /** Actualiza uno existente. */
    @EsStaff
    @PutMapping("/gamificacion/misiones/{id}")
    public MisionDto actualizar(@PathVariable("id") Long id, @Valid @RequestBody MisionDto dto) {
        return misionUseCase.actualizar(id, dto);
    }

    /** Activa o desactiva sin borrar (?valor=true|false). */
    @EsStaff
    @PatchMapping("/gamificacion/misiones/{id}/activo")
    public MisionDto cambiarActivo(@PathVariable("id") Long id, @RequestParam("valor") boolean valor) {
        return misionUseCase.cambiarActivo(id, valor);
    }

    /** Elimina (409 si tiene datos relacionados; en ese caso usa desactivar). */
    @EsAdmin
    @DeleteMapping("/gamificacion/misiones/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable("id") Long id) {
        misionUseCase.eliminar(id);
    }
}
