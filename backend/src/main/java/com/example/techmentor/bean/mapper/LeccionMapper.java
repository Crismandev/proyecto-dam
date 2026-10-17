package com.example.techmentor.bean.mapper;

import org.mapstruct.*;
import com.example.techmentor.bean.dto.LeccionDto;
import com.example.techmentor.bean.entity.Leccion;

/** Mapper MapStruct: convierte Leccion <-> LeccionDto (las relaciones se resuelven en el Model). */
@Mapper(componentModel = "spring")
public interface LeccionMapper {

    @Mapping(source = "nivel.idNivel", target = "idNivel")
    LeccionDto toDto(Leccion entity);

    @Mapping(target = "idLeccion", ignore = true)
    @Mapping(target = "nivel", ignore = true)
    @Mapping(target = "xpRecompensa", source = "xpRecompensa", defaultValue = "0")
    Leccion toEntity(LeccionDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "idLeccion", ignore = true)
    @Mapping(target = "nivel", ignore = true)
    void actualizar(LeccionDto dto, @MappingTarget Leccion entity);
}
