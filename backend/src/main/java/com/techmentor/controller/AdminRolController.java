package com.techmentor.controller;

import com.techmentor.dto.RolDTO;
import com.techmentor.service.RolService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/roles")
@RequiredArgsConstructor
public class AdminRolController {

    private final RolService rolService;

    @GetMapping
    public ResponseEntity<Page<RolDTO>> listar(
            @RequestParam(required = false, defaultValue = "") String q,
            Pageable pageable) {
        return ResponseEntity.ok(rolService.listar(q, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RolDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(rolService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<RolDTO> crear(@Valid @RequestBody RolDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(rolService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RolDTO> actualizar(@PathVariable Long id, @Valid @RequestBody RolDTO dto) {
        return ResponseEntity.ok(rolService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        rolService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
