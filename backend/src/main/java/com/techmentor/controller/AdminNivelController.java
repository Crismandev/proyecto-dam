package com.techmentor.controller;

import com.techmentor.dto.NivelDTO;
import com.techmentor.dto.NivelSaveDTO;
import com.techmentor.service.NivelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/niveles")
@RequiredArgsConstructor
public class AdminNivelController {

    private final NivelService nivelService;

    @GetMapping
    public ResponseEntity<Page<NivelDTO>> listar(
            @RequestParam(required = false) Long idCurso,
            @RequestParam(required = false, defaultValue = "") String q,
            Pageable pageable) {
        return ResponseEntity.ok(nivelService.listar(idCurso, q, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NivelDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(nivelService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<NivelDTO> crear(@Valid @RequestBody NivelSaveDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(nivelService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NivelDTO> actualizar(@PathVariable Long id, @Valid @RequestBody NivelSaveDTO dto) {
        return ResponseEntity.ok(nivelService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        nivelService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
