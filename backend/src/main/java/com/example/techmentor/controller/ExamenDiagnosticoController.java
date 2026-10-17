package com.example.techmentor.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.example.techmentor.bean.dto.*;
import com.example.techmentor.security.*;
import com.example.techmentor.usecase.*;

/** Endpoints REST de examen diagnostico. */
@RestController
@RequestMapping("/diagnostico/examenes")
@RequiredArgsConstructor
public class ExamenDiagnosticoController {

    private final ExamenDiagnosticoUseCase examenUseCase;

    /** Exámenes activos (filtros opcionales ?idCurso= y ?idProfesion=). Cualquier usuario autenticado. */
    @GetMapping
    public List<ExamenDiagnosticoDto> listarActivos(
            @RequestParam(name = "idCurso", required = false) Long idCurso,
            @RequestParam(name = "idProfesion", required = false) Long idProfesion) {
        return examenUseCase.listarActivos(idCurso, idProfesion);
    }

    /** Exámenes adaptados al puesto (profesión principal) del usuario autenticado. */
    @EsEstudiante
    @GetMapping("/para-mi")
    public List<ExamenDiagnosticoDto> listarParaMi(@AuthenticationPrincipal UsuarioAutenticado usuario) {
        return examenUseCase.listarParaUsuario(usuario.idUsuario());
    }

    /** Incluye inactivos. */
    @EsStaff
    @GetMapping("/todos")
    public List<ExamenDiagnosticoDto> listarTodos() {
        return examenUseCase.listarTodos();
    }

    @GetMapping("/{id}")
    public ExamenDiagnosticoDto obtener(@PathVariable("id") Long id) {
        return examenUseCase.obtener(id);
    }

    /** Examen con preguntas y opciones sin la respuesta correcta. Cualquier usuario autenticado. */
    @GetMapping("/{id}/rendir")
    public ExamenParaRendirDto obtenerParaRendir(@PathVariable("id") Long id) {
        return examenUseCase.obtenerParaRendir(id);
    }

    @EsStaff
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ExamenDiagnosticoDto crear(@Valid @RequestBody ExamenDiagnosticoDto dto) {
        return examenUseCase.crear(dto);
    }

    @EsStaff
    @PutMapping("/{id}")
    public ExamenDiagnosticoDto actualizar(@PathVariable("id") Long id,
                                           @Valid @RequestBody ExamenDiagnosticoDto dto) {
        return examenUseCase.actualizar(id, dto);
    }

    @EsStaff
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable("id") Long id) {
        examenUseCase.eliminar(id);
    }
}
