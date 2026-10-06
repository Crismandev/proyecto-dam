package com.techmentor.controller;

import com.techmentor.dto.LeccionDTO;
import com.techmentor.dto.LeccionSaveDTO;
import com.techmentor.service.LeccionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/lecciones")
@RequiredArgsConstructor
public class AdminLeccionController {

    private final LeccionService leccionService;

    @GetMapping
    public ResponseEntity<Page<LeccionDTO>> listar(
            @RequestParam(required = false) Long idNivel,
            @RequestParam(required = false, defaultValue = "") String q,
            Pageable pageable) {
        return ResponseEntity.ok(leccionService.listar(idNivel, q, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LeccionDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(leccionService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<LeccionDTO> crear(@Valid @RequestBody LeccionSaveDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(leccionService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LeccionDTO> actualizar(@PathVariable Long id, @Valid @RequestBody LeccionSaveDTO dto) {
        return ResponseEntity.ok(leccionService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        leccionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
