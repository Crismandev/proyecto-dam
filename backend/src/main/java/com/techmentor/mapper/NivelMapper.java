package com.techmentor.mapper;

import com.techmentor.dto.NivelDTO;
import com.techmentor.entity.Nivel;
import org.springframework.stereotype.Component;

@Component
public class NivelMapper {

    public NivelDTO toDTO(Nivel entity) {
        if (entity == null) return null;
        return NivelDTO.builder()
                .idNivel(entity.getIdNivel())
                .idCurso(entity.getCurso() != null ? entity.getCurso().getIdCurso() : null)
                .nombreCurso(entity.getCurso() != null ? entity.getCurso().getNombre() : null)
                .nombreNivel(entity.getNombreNivel())
                .orden(entity.getOrden())
                .descripcion(entity.getDescripcion())
                .build();
    }
}
