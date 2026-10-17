package com.example.techmentor.security;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/** Principal que el JwtFilter deja en el SecurityContext. */
public record UsuarioAutenticado(Long idUsuario, String username, String rol) {

    public boolean esStaff() {
        return "ADMINISTRADOR".equals(rol) || "MAESTRO".equals(rol);
    }

    /** Permite el acceso solo al dueño del recurso o a ADMINISTRADOR / MAESTRO. */
    public void exigirPropietarioOStaff(Long idPropietario) {
        if (!esStaff() && !idUsuario.equals(idPropietario)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes acceso a este recurso");
        }
    }
}
