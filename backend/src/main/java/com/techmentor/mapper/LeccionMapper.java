package com.techmentor.mapper;

import com.techmentor.dto.LeccionDTO;
import com.techmentor.entity.Leccion;
import org.springframework.stereotype.Component;

@Component
public class LeccionMapper {

    public LeccionDTO toDTO(Leccion entity) {
        if (entity == null) return null;
        Long idCurso = null;
        String nombreCurso = null;
        if (entity.getNivel() != null && entity.getNivel().getCurso() != null) {
            idCurso = entity.getNivel().getCurso().getIdCurso();
            nombreCurso = entity.getNivel().getCurso().getNombre();
        }

        return LeccionDTO.builder()
                .idLeccion(entity.getIdLeccion())
                .idNivel(entity.getNivel() != null ? entity.getNivel().getIdNivel() : null)
                .nombreNivel(entity.getNivel() != null ? entity.getNivel().getNombreNivel() : null)
                .idCurso(idCurso)
                .nombreCurso(nombreCurso)
                .titulo(entity.getTitulo())
                .orden(entity.getOrden())
                .descripcion(entity.getDescripcion())
                .build();
    }
}
