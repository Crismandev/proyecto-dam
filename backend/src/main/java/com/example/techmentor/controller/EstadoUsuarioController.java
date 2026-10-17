package com.example.techmentor.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.example.techmentor.bean.dto.EstadoUsuarioDto;
import com.example.techmentor.security.*;
import com.example.techmentor.usecase.EstadoUsuarioUseCase;

/** Endpoints REST de estado de usuario (/seguridad/estados-usuario). Los permisos por rol van en cada método. */
@RestController
@RequiredArgsConstructor
public class EstadoUsuarioController {

    private final EstadoUsuarioUseCase estadoUsuarioUseCase;

    /** Lista todos los registros. */
    @EsStaff
    @GetMapping("/seguridad/estados-usuario")
    public List<EstadoUsuarioDto> listar() {
        return estadoUsuarioUseCase.listar();
    }

    /** Obtiene uno por id. */
    @EsStaff
    @GetMapping("/seguridad/estados-usuario/{id}")
    public EstadoUsuarioDto obtener(@PathVariable("id") Long id) {
        return estadoUsuarioUseCase.obtener(id);
    }

    /** Crea uno nuevo. */
    @EsAdmin
    @PostMapping("/seguridad/estados-usuario")
    @ResponseStatus(HttpStatus.CREATED)
    public EstadoUsuarioDto crear(@Valid @RequestBody EstadoUsuarioDto dto) {
        return estadoUsuarioUseCase.crear(dto);
    }

    /** Actualiza uno existente. */
    @EsAdmin
    @PutMapping("/seguridad/estados-usuario/{id}")
    public EstadoUsuarioDto actualizar(@PathVariable("id") Long id, @Valid @RequestBody EstadoUsuarioDto dto) {
        return estadoUsuarioUseCase.actualizar(id, dto);
    }

    /** Elimina (409 si tiene datos relacionados; en ese caso usa desactivar). */
    @EsAdmin
    @DeleteMapping("/seguridad/estados-usuario/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable("id") Long id) {
        estadoUsuarioUseCase.eliminar(id);
    }
}
