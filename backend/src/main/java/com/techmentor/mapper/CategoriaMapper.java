package com.techmentor.mapper;

import com.techmentor.dto.CategoriaDTO;
import com.techmentor.entity.Categoria;
import org.springframework.stereotype.Component;

@Component
public class CategoriaMapper {

    public CategoriaDTO toDTO(Categoria entity) {
        if (entity == null) return null;
        return CategoriaDTO.builder()
                .idCategoria(entity.getIdCategoria())
                .nombreCategoria(entity.getNombre())
                .descripcion(entity.getDescripcion())
                .icono(entity.getIcono())
                .build();
    }

    public Categoria toEntity(CategoriaDTO dto) {
        if (dto == null) return null;
        Categoria entity = new Categoria();
        entity.setIdCategoria(dto.getIdCategoria());
        entity.setNombre(dto.getNombreCategoria());
        entity.setDescripcion(dto.getDescripcion());
        entity.setIcono(dto.getIcono());
        return entity;
    }
}
