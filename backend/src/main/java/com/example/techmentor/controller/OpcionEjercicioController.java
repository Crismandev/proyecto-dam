package com.example.techmentor.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.example.techmentor.bean.dto.OpcionEjercicioDto;
import com.example.techmentor.security.*;
import com.example.techmentor.usecase.OpcionEjercicioUseCase;

/** Endpoints REST de opción de ejercicio (/catalogo/opciones-ejercicio). Los permisos por rol van en cada método. */
@RestController
@RequiredArgsConstructor
public class OpcionEjercicioController {

    private final OpcionEjercicioUseCase opcionEjercicioUseCase;

    /** Lista todos los registros. */
    @EsStaff
    @GetMapping("/catalogo/opciones-ejercicio")
    public List<OpcionEjercicioDto> listar() {
        return opcionEjercicioUseCase.listar();
    }

    /** Lista los registros de un(a) ejercicio. */
    @EsStaff
    @GetMapping("/catalogo/ejercicios/{idEjercicio}/opciones")
    public List<OpcionEjercicioDto> listarPorEjercicio(@PathVariable("idEjercicio") Long idEjercicio) {
        return opcionEjercicioUseCase.listarPorEjercicio(idEjercicio);
    }

    /** Obtiene uno por id. */
    @EsStaff
    @GetMapping("/catalogo/opciones-ejercicio/{id}")
    public OpcionEjercicioDto obtener(@PathVariable("id") Long id) {
        return opcionEjercicioUseCase.obtener(id);
    }

    /** Crea uno nuevo. */
    @EsStaff
    @PostMapping("/catalogo/opciones-ejercicio")
    @ResponseStatus(HttpStatus.CREATED)
    public OpcionEjercicioDto crear(@Valid @RequestBody OpcionEjercicioDto dto) {
        return opcionEjercicioUseCase.crear(dto);
    }

    /** Actualiza uno existente. */
    @EsStaff
    @PutMapping("/catalogo/opciones-ejercicio/{id}")
    public OpcionEjercicioDto actualizar(@PathVariable("id") Long id, @Valid @RequestBody OpcionEjercicioDto dto) {
        return opcionEjercicioUseCase.actualizar(id, dto);
    }

    /** Elimina (409 si tiene datos relacionados; en ese caso usa desactivar). */
    @EsStaff
    @DeleteMapping("/catalogo/opciones-ejercicio/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable("id") Long id) {
        opcionEjercicioUseCase.eliminar(id);
    }
}
