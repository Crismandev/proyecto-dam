package com.example.techmentor.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.example.techmentor.bean.dto.EjercicioDto;
import com.example.techmentor.security.*;
import com.example.techmentor.usecase.EjercicioUseCase;

/** Endpoints REST de ejercicio (/catalogo/ejercicios). Los permisos por rol van en cada método. */
@RestController
@RequiredArgsConstructor
public class EjercicioController {

    private final EjercicioUseCase ejercicioUseCase;

    /** Lista todos los registros. */
    @EsStaff
    @GetMapping("/catalogo/ejercicios")
    public List<EjercicioDto> listar() {
        return ejercicioUseCase.listar();
    }

    /** Lista los registros de un(a) leccion. */
    @EsStaff
    @GetMapping("/catalogo/lecciones/{idLeccion}/ejercicios")
    public List<EjercicioDto> listarPorLeccion(@PathVariable("idLeccion") Long idLeccion) {
        return ejercicioUseCase.listarPorLeccion(idLeccion);
    }

    /** Obtiene uno por id. */
    @EsStaff
    @GetMapping("/catalogo/ejercicios/{id}")
    public EjercicioDto obtener(@PathVariable("id") Long id) {
        return ejercicioUseCase.obtener(id);
    }

    /** Crea uno nuevo. */
    @EsStaff
    @PostMapping("/catalogo/ejercicios")
    @ResponseStatus(HttpStatus.CREATED)
    public EjercicioDto crear(@Valid @RequestBody EjercicioDto dto) {
        return ejercicioUseCase.crear(dto);
    }

    /** Actualiza uno existente. */
    @EsStaff
    @PutMapping("/catalogo/ejercicios/{id}")
    public EjercicioDto actualizar(@PathVariable("id") Long id, @Valid @RequestBody EjercicioDto dto) {
        return ejercicioUseCase.actualizar(id, dto);
    }

    /** Elimina (409 si tiene datos relacionados; en ese caso usa desactivar). */
    @EsStaff
    @DeleteMapping("/catalogo/ejercicios/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable("id") Long id) {
        ejercicioUseCase.eliminar(id);
    }
}
