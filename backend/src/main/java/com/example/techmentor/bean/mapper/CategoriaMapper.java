package com.example.techmentor.bean.mapper;

import org.mapstruct.*;
import com.example.techmentor.bean.dto.CategoriaDto;
import com.example.techmentor.bean.entity.Categoria;

/** Mapper MapStruct: convierte Categoria <-> CategoriaDto (las relaciones se resuelven en el Model). */
@Mapper(componentModel = "spring")
public interface CategoriaMapper {

    CategoriaDto toDto(Categoria entity);

    @Mapping(target = "idCategoria", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "activo", source = "activo", defaultValue = "true")
    Categoria toEntity(CategoriaDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "idCategoria", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    void actualizar(CategoriaDto dto, @MappingTarget Categoria entity);
}
