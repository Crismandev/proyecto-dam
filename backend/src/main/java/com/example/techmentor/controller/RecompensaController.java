package com.example.techmentor.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.example.techmentor.bean.dto.RecompensaDto;
import com.example.techmentor.security.*;
import com.example.techmentor.usecase.RecompensaUseCase;

/** Endpoints REST de recompensa (/gamificacion/recompensas). Los permisos por rol van en cada método. */
@RestController
@RequiredArgsConstructor
public class RecompensaController {

    private final RecompensaUseCase recompensaUseCase;

    /** Lista solo los activos. */
    @GetMapping("/gamificacion/recompensas")
    public List<RecompensaDto> listarActivos() {
        return recompensaUseCase.listarActivos();
    }

    /** Lista todos, incluidos los inactivos. */
    @EsStaff
    @GetMapping("/gamificacion/recompensas/todos")
    public List<RecompensaDto> listarTodos() {
        return recompensaUseCase.listarTodos();
    }

    /** Obtiene uno por id. */
    @GetMapping("/gamificacion/recompensas/{id}")
    public RecompensaDto obtener(@PathVariable("id") Long id) {
        return recompensaUseCase.obtener(id);
    }

    /** Crea uno nuevo. */
    @EsStaff
    @PostMapping("/gamificacion/recompensas")
    @ResponseStatus(HttpStatus.CREATED)
    public RecompensaDto crear(@Valid @RequestBody RecompensaDto dto) {
        return recompensaUseCase.crear(dto);
    }

    /** Actualiza uno existente. */
    @EsStaff
    @PutMapping("/gamificacion/recompensas/{id}")
    public RecompensaDto actualizar(@PathVariable("id") Long id, @Valid @RequestBody RecompensaDto dto) {
        return recompensaUseCase.actualizar(id, dto);
    }

    /** Activa o desactiva sin borrar (?valor=true|false). */
    @EsStaff
    @PatchMapping("/gamificacion/recompensas/{id}/activo")
    public RecompensaDto cambiarActivo(@PathVariable("id") Long id, @RequestParam("valor") boolean valor) {
        return recompensaUseCase.cambiarActivo(id, valor);
    }

    /** Elimina (409 si tiene datos relacionados; en ese caso usa desactivar). */
    @EsAdmin
    @DeleteMapping("/gamificacion/recompensas/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable("id") Long id) {
        recompensaUseCase.eliminar(id);
    }
}
