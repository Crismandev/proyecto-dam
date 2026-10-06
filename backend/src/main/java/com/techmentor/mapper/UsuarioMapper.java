package com.techmentor.mapper;

import com.techmentor.dto.UsuarioDTO;
import com.techmentor.entity.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public UsuarioDTO toDTO(Usuario entity) {
        if (entity == null) return null;
        return UsuarioDTO.builder()
                .idUsuario(entity.getIdUsuario())
                .idRol(entity.getRol() != null ? entity.getRol().getIdRol() : null)
                .nombreRol(entity.getRol() != null ? entity.getRol().getNombre() : null)
                .nombres(entity.getNombres())
                .apellidos(entity.getApellidos())
                .correo(entity.getEmail())
                .fechaNacimiento(entity.getFechaNacimiento())
                .avatar(entity.getAvatar())
                .xpTotal(entity.getXpTotal())
                .monedas(entity.getMonedas())
                .build();
    }
}
