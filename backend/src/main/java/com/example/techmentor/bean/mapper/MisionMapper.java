package com.example.techmentor.bean.mapper;

import org.mapstruct.*;
import com.example.techmentor.bean.dto.MisionDto;
import com.example.techmentor.bean.entity.Mision;

/** Mapper MapStruct: convierte Mision <-> MisionDto (las relaciones se resuelven en el Model). */
@Mapper(componentModel = "spring")
public interface MisionMapper {

    MisionDto toDto(Mision entity);

    @Mapping(target = "idMision", ignore = true)
    @Mapping(target = "xpRecompensa", source = "xpRecompensa", defaultValue = "0")
    @Mapping(target = "activa", source = "activa", defaultValue = "true")
    Mision toEntity(MisionDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "idMision", ignore = true)
    void actualizar(MisionDto dto, @MappingTarget Mision entity);
}
