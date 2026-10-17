package com.example.techmentor.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.example.techmentor.bean.dto.CategoriaPreguntaCursoDto;
import com.example.techmentor.security.*;
import com.example.techmentor.usecase.CategoriaPreguntaCursoUseCase;

/** Endpoints REST de relación categoría de pregunta-curso (/diagnostico/categoria-pregunta-cursos). Los permisos por rol van en cada método. */
@RestController
@RequiredArgsConstructor
public class CategoriaPreguntaCursoController {

    private final CategoriaPreguntaCursoUseCase categoriaPreguntaCursoUseCase;

    /** Lista todos los registros. */
    @EsStaff
    @GetMapping("/diagnostico/categoria-pregunta-cursos")
    public List<CategoriaPreguntaCursoDto> listar() {
        return categoriaPreguntaCursoUseCase.listar();
    }

    /** Lista los registros de un(a) categoriaPregunta. */
    @EsStaff
    @GetMapping("/diagnostico/categorias-pregunta/{idCategoriaPregunta}/cursos")
    public List<CategoriaPreguntaCursoDto> listarPorCategoriaPregunta(@PathVariable("idCategoriaPregunta") Long idCategoriaPregunta) {
        return categoriaPreguntaCursoUseCase.listarPorCategoriaPregunta(idCategoriaPregunta);
    }

    /** Obtiene uno por id. */
    @EsStaff
    @GetMapping("/diagnostico/categoria-pregunta-cursos/{id}")
    public CategoriaPreguntaCursoDto obtener(@PathVariable("id") Long id) {
        return categoriaPreguntaCursoUseCase.obtener(id);
    }

    /** Crea uno nuevo. */
    @EsStaff
    @PostMapping("/diagnostico/categoria-pregunta-cursos")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoriaPreguntaCursoDto crear(@Valid @RequestBody CategoriaPreguntaCursoDto dto) {
        return categoriaPreguntaCursoUseCase.crear(dto);
    }

    /** Actualiza uno existente. */
    @EsStaff
    @PutMapping("/diagnostico/categoria-pregunta-cursos/{id}")
    public CategoriaPreguntaCursoDto actualizar(@PathVariable("id") Long id, @Valid @RequestBody CategoriaPreguntaCursoDto dto) {
        return categoriaPreguntaCursoUseCase.actualizar(id, dto);
    }

    /** Elimina (409 si tiene datos relacionados; en ese caso usa desactivar). */
    @EsStaff
    @DeleteMapping("/diagnostico/categoria-pregunta-cursos/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable("id") Long id) {
        categoriaPreguntaCursoUseCase.eliminar(id);
    }
}
