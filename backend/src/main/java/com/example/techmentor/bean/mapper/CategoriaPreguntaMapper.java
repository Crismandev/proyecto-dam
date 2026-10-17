package com.example.techmentor.bean.mapper;

import org.mapstruct.*;
import com.example.techmentor.bean.dto.*;
import com.example.techmentor.bean.entity.*;

/** Mapper MapStruct: convierte categoria pregunta <-> DTO. */
@Mapper(componentModel = "spring")
public interface CategoriaPreguntaMapper {

    CategoriaPreguntaDto toDto(CategoriaPregunta entity);

    @Mapping(target = "idCategoriaPregunta", ignore = true)
    CategoriaPregunta toEntity(CategoriaPreguntaDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "idCategoriaPregunta", ignore = true)
    void actualizar(CategoriaPreguntaDto dto, @MappingTarget CategoriaPregunta entity);
}
