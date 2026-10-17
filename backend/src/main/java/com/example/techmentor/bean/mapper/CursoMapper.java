package com.example.techmentor.bean.mapper;

import org.mapstruct.*;
import com.example.techmentor.bean.dto.CursoDto;
import com.example.techmentor.bean.entity.Curso;

/** Mapper MapStruct: convierte Curso <-> CursoDto (las relaciones se resuelven en el Model). */
@Mapper(componentModel = "spring")
public interface CursoMapper {

    @Mapping(source = "categoria.idCategoria", target = "idCategoria")
    CursoDto toDto(Curso entity);

    @Mapping(target = "idCurso", ignore = true)
    @Mapping(target = "categoria", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "fechaActualizacion", ignore = true)
    @Mapping(target = "xpRequerido", source = "xpRequerido", defaultValue = "0")
    @Mapping(target = "xpRecompensa", source = "xpRecompensa", defaultValue = "0")
    @Mapping(target = "activo", source = "activo", defaultValue = "true")
    Curso toEntity(CursoDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "idCurso", ignore = true)
    @Mapping(target = "categoria", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "fechaActualizacion", ignore = true)
    void actualizar(CursoDto dto, @MappingTarget Curso entity);
}
