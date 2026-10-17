package com.example.techmentor.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.example.techmentor.bean.dto.*;
import com.example.techmentor.security.*;
import com.example.techmentor.usecase.*;

/** Endpoints de autenticación: login (público) y cambio de contraseña. */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthUseCase authUseCase;

    /** Público. Devuelve el JWT que se envía como "Authorization: Bearer <token>". */
    @PostMapping("/login")
    public TokenDto login(@Valid @RequestBody LoginDto dto) {
        return authUseCase.login(dto);
    }

    /** Cambia la contraseña del usuario autenticado. */
    @PutMapping("/cambiar-clave")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cambiarClave(@Valid @RequestBody CambiarClaveDto dto,
                             @AuthenticationPrincipal UsuarioAutenticado usuario) {
        authUseCase.cambiarClave(usuario, dto);
    }
}
