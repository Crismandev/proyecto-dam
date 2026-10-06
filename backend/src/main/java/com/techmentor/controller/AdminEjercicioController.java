package com.techmentor.controller;

import com.techmentor.dto.EjercicioDTO;
import com.techmentor.dto.EjercicioSaveDTO;
import com.techmentor.service.EjercicioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/ejercicios")
@RequiredArgsConstructor
public class AdminEjercicioController {

    private final EjercicioService ejercicioService;

    @GetMapping
    public ResponseEntity<Page<EjercicioDTO>> listar(
            @RequestParam(required = false) Long idLeccion,
            @RequestParam(required = false, defaultValue = "") String q,
            Pageable pageable) {
        return ResponseEntity.ok(ejercicioService.listar(idLeccion, q, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EjercicioDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ejercicioService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<EjercicioDTO> crear(@Valid @RequestBody EjercicioSaveDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ejercicioService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EjercicioDTO> actualizar(@PathVariable Long id, @Valid @RequestBody EjercicioSaveDTO dto) {
        return ResponseEntity.ok(ejercicioService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        ejercicioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
