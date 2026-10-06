package com.techmentor.controller;

import com.techmentor.dto.*;
import com.techmentor.service.MobileApiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mobile")
@RequiredArgsConstructor
public class MobileApiController {

    private final MobileApiService mobileApiService;
    private final com.techmentor.repository.UsuarioRepository usuarioRepository;

    private Long getUsuarioId(Authentication authentication) {
        String identifier = authentication.getName();
        return usuarioRepository.findByIdentificador(identifier)
                .orElseThrow(() -> new com.techmentor.exception.ResourceNotFoundException("Usuario no encontrado: " + identifier))
                .getIdUsuario();
    }

    @GetMapping("/perfil")
    public ResponseEntity<MobilePerfilDTO> obtenerPerfil(Authentication authentication) {
        return ResponseEntity.ok(mobileApiService.obtenerPerfil(getUsuarioId(authentication)));
    }

    @GetMapping("/cursos")
    public ResponseEntity<List<MobileCursoDTO>> listarCursosActivos(Authentication authentication) {
        return ResponseEntity.ok(mobileApiService.listarCursosActivos(getUsuarioId(authentication)));
    }

    @GetMapping("/cursos/{id}/ruta")
    public ResponseEntity<MobileRutaDTO> obtenerRutaCurso(@PathVariable Long id, Authentication authentication) {
        return ResponseEntity.ok(mobileApiService.obtenerRutaCurso(id, getUsuarioId(authentication)));
    }

    @GetMapping("/lecciones/{id}")
    public ResponseEntity<MobileLeccionDTO> obtenerDetalleLeccion(@PathVariable Long id) {
        return ResponseEntity.ok(mobileApiService.obtenerDetalleLeccion(id));
    }

    @PostMapping("/evaluacion")
    public ResponseEntity<MobileEvaluacionDTO> evaluarEjercicio(@RequestBody MobileEvaluacionRequest request) {
        return ResponseEntity.ok(mobileApiService.evaluarEjercicio(request.getIdEjercicio(), request.getIdOpcionSeleccionada()));
    }

    @PostMapping("/entrevista")
    public ResponseEntity<MobileEntrevistaDTO> procesarEntrevista(@RequestBody MobileEntrevistaRequest request) {
        return ResponseEntity.ok(mobileApiService.procesarEntrevista(request.getIdEjercicio(), request.getInputEstudiante()));
    }

    @PostMapping("/progreso/sincronizar")
    public ResponseEntity<ProgresoLeccionDTO> sincronizarProgreso(
            @Valid @RequestBody ProgresoSyncDTO request,
            Authentication authentication) {
        return ResponseEntity.ok(mobileApiService.sincronizarProgreso(getUsuarioId(authentication), request));
    }

    @lombok.Data
    public static class MobileEvaluacionRequest {
        private Long idEjercicio;
        private Long idOpcionSeleccionada;
    }

    @lombok.Data
    public static class MobileEntrevistaRequest {
        private Long idEjercicio;
        private String inputEstudiante;
    }
}
