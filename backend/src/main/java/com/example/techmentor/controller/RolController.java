package com.example.techmentor.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.example.techmentor.bean.dto.RolDto;
import com.example.techmentor.security.*;
import com.example.techmentor.usecase.RolUseCase;

/** Endpoints REST de rol (/seguridad/roles). Los permisos por rol van en cada método. */
@RestController
@RequiredArgsConstructor
public class RolController {

    private final RolUseCase rolUseCase;

    /** Lista todos los registros. */
    @EsStaff
    @GetMapping("/seguridad/roles")
    public List<RolDto> listar() {
        return rolUseCase.listar();
    }

    /** Obtiene uno por id. */
    @EsStaff
    @GetMapping("/seguridad/roles/{id}")
    public RolDto obtener(@PathVariable("id") Long id) {
        return rolUseCase.obtener(id);
    }

    /** Crea uno nuevo. */
    @EsAdmin
    @PostMapping("/seguridad/roles")
    @ResponseStatus(HttpStatus.CREATED)
    public RolDto crear(@Valid @RequestBody RolDto dto) {
        return rolUseCase.crear(dto);
    }

    /** Actualiza uno existente. */
    @EsAdmin
    @PutMapping("/seguridad/roles/{id}")
    public RolDto actualizar(@PathVariable("id") Long id, @Valid @RequestBody RolDto dto) {
        return rolUseCase.actualizar(id, dto);
    }

    /** Elimina (409 si tiene datos relacionados; en ese caso usa desactivar). */
    @EsAdmin
    @DeleteMapping("/seguridad/roles/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable("id") Long id) {
        rolUseCase.eliminar(id);
    }
}
