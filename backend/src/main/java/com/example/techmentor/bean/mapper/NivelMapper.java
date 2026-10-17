package com.example.techmentor.bean.mapper;

import org.mapstruct.*;
import com.example.techmentor.bean.dto.NivelDto;
import com.example.techmentor.bean.entity.Nivel;

/** Mapper MapStruct: convierte Nivel <-> NivelDto (las relaciones se resuelven en el Model). */
@Mapper(componentModel = "spring")
public interface NivelMapper {

    @Mapping(source = "curso.idCurso", target = "idCurso")
    NivelDto toDto(Nivel entity);

    @Mapping(target = "idNivel", ignore = true)
    @Mapping(target = "curso", ignore = true)
    Nivel toEntity(NivelDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "idNivel", ignore = true)
    @Mapping(target = "curso", ignore = true)
    void actualizar(NivelDto dto, @MappingTarget Nivel entity);
}
