package com.techmentor.controller;

import com.techmentor.dto.ProgresoLeccionDTO;
import com.techmentor.service.ProgresoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/progreso")
@RequiredArgsConstructor
public class AdminProgresoController {

    private final ProgresoService progresoService;

    @GetMapping
    public ResponseEntity<Page<ProgresoLeccionDTO>> listar(
            @RequestParam(required = false) Long idUsuario,
            @RequestParam(required = false) Long idLeccion,
            Pageable pageable) {
        return ResponseEntity.ok(progresoService.listar(idUsuario, idLeccion, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProgresoLeccionDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(progresoService.obtenerPorId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        progresoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
