package com.techmentor.mapper;

import com.techmentor.dto.RolDTO;
import com.techmentor.entity.Rol;
import org.springframework.stereotype.Component;

@Component
public class RolMapper {

    public RolDTO toDTO(Rol entity) {
        if (entity == null) return null;
        return RolDTO.builder()
                .idRol(entity.getIdRol())
                .nombreRol(entity.getNombre())
                .build();
    }

    public Rol toEntity(RolDTO dto) {
        if (dto == null) return null;
        Rol entity = new Rol();
        entity.setIdRol(dto.getIdRol());
        entity.setNombre(dto.getNombreRol());
        return entity;
    }
}
