package com.example.techmentor.usecase;

import com.example.techmentor.bean.dto.CambiarClaveDto;
import com.example.techmentor.bean.dto.LoginDto;
import com.example.techmentor.bean.dto.TokenDto;
import com.example.techmentor.security.UsuarioAutenticado;

/** Casos de uso de autenticación: login con usuario y contraseña, y cambio de contraseña. */
public interface AuthUseCase {

    /** Valida credenciales y estado ACTIVO; devuelve el JWT. */
    TokenDto login(LoginDto dto);

    /** Comprueba que la cuenta del JWT siga activa y obtiene sus permisos actuales. */
    UsuarioAutenticado validarSesion(Long idUsuario, String username);

    /** Cambia la contraseña del usuario autenticado (exige la clave actual). */
    void cambiarClave(UsuarioAutenticado usuario, CambiarClaveDto dto);
}
