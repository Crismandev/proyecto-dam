package com.example.techmentor.bean.mapper;

import org.mapstruct.*;
import com.example.techmentor.bean.dto.ProfesionDto;
import com.example.techmentor.bean.entity.Profesion;

/** Mapper MapStruct: convierte Profesion <-> ProfesionDto (las relaciones se resuelven en el Model). */
@Mapper(componentModel = "spring")
public interface ProfesionMapper {

    ProfesionDto toDto(Profesion entity);

    @Mapping(target = "idProfesion", ignore = true)
    @Mapping(target = "activo", source = "activo", defaultValue = "true")
    Profesion toEntity(ProfesionDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "idProfesion", ignore = true)
    void actualizar(ProfesionDto dto, @MappingTarget Profesion entity);
}
