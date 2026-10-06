package com.techmentor.controller;

import com.techmentor.dto.CursoDTO;
import com.techmentor.dto.CursoSaveDTO;
import com.techmentor.service.CursoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/cursos")
@RequiredArgsConstructor
public class AdminCursoController {

    private final CursoService cursoService;

    @GetMapping
    public ResponseEntity<Page<CursoDTO>> listar(
            @RequestParam(required = false) Long idCategoria,
            @RequestParam(required = false, defaultValue = "") String q,
            Pageable pageable) {
        return ResponseEntity.ok(cursoService.listar(idCategoria, q, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CursoDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(cursoService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<CursoDTO> crear(@Valid @RequestBody CursoSaveDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cursoService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CursoDTO> actualizar(@PathVariable Long id, @Valid @RequestBody CursoSaveDTO dto) {
        return ResponseEntity.ok(cursoService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        cursoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
