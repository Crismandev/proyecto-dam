package com.example.techmentor.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.example.techmentor.bean.dto.ProfesionCursoDto;
import com.example.techmentor.security.*;
import com.example.techmentor.usecase.ProfesionCursoUseCase;

/** Endpoints REST de relación puesto-curso (/catalogo/profesion-cursos). Los permisos por rol van en cada método. */
@RestController
@RequiredArgsConstructor
public class ProfesionCursoController {

    private final ProfesionCursoUseCase profesionCursoUseCase;

    /** Lista todos los registros. */
    @GetMapping("/catalogo/profesion-cursos")
    public List<ProfesionCursoDto> listar() {
        return profesionCursoUseCase.listar();
    }

    /** Lista los registros de un(a) profesion. */
    @GetMapping("/catalogo/profesiones/{idProfesion}/cursos")
    public List<ProfesionCursoDto> listarPorProfesion(@PathVariable("idProfesion") Long idProfesion) {
        return profesionCursoUseCase.listarPorProfesion(idProfesion);
    }

    /** Obtiene uno por id. */
    @GetMapping("/catalogo/profesion-cursos/{id}")
    public ProfesionCursoDto obtener(@PathVariable("id") Long id) {
        return profesionCursoUseCase.obtener(id);
    }

    /** Crea uno nuevo. */
    @EsStaff
    @PostMapping("/catalogo/profesion-cursos")
    @ResponseStatus(HttpStatus.CREATED)
    public ProfesionCursoDto crear(@Valid @RequestBody ProfesionCursoDto dto) {
        return profesionCursoUseCase.crear(dto);
    }

    /** Actualiza uno existente. */
    @EsStaff
    @PutMapping("/catalogo/profesion-cursos/{id}")
    public ProfesionCursoDto actualizar(@PathVariable("id") Long id, @Valid @RequestBody ProfesionCursoDto dto) {
        return profesionCursoUseCase.actualizar(id, dto);
    }

    /** Elimina (409 si tiene datos relacionados; en ese caso usa desactivar). */
    @EsStaff
    @DeleteMapping("/catalogo/profesion-cursos/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable("id") Long id) {
        profesionCursoUseCase.eliminar(id);
    }
}
