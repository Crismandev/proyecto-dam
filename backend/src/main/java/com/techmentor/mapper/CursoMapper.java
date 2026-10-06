package com.techmentor.mapper;

import com.techmentor.dto.CursoDTO;
import com.techmentor.entity.Curso;
import org.springframework.stereotype.Component;

@Component
public class CursoMapper {

    public CursoDTO toDTO(Curso entity) {
        if (entity == null) return null;
        return CursoDTO.builder()
                .idCurso(entity.getIdCurso())
                .idCategoria(entity.getCategoria() != null ? entity.getCategoria().getIdCategoria() : null)
                .nombreCategoria(entity.getCategoria() != null ? entity.getCategoria().getNombre() : null)
                .nombreCurso(entity.getNombre())
                .descripcion(entity.getDescripcion())
                .icono(entity.getIcono())
                .dificultad(null)
                .xpRequerido(entity.getXpRequerido())
                .xpRecompensa(entity.getXpRecompensa())
                .build();
    }
}
