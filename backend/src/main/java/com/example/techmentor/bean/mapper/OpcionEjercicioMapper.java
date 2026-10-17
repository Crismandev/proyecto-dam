package com.example.techmentor.bean.mapper;

import org.mapstruct.*;
import com.example.techmentor.bean.dto.OpcionEjercicioDto;
import com.example.techmentor.bean.entity.OpcionEjercicio;

/** Mapper MapStruct: convierte OpcionEjercicio <-> OpcionEjercicioDto (las relaciones se resuelven en el Model). */
@Mapper(componentModel = "spring")
public interface OpcionEjercicioMapper {

    @Mapping(source = "ejercicio.idEjercicio", target = "idEjercicio")
    OpcionEjercicioDto toDto(OpcionEjercicio entity);

    @Mapping(target = "idOpcion", ignore = true)
    @Mapping(target = "ejercicio", ignore = true)
    @Mapping(target = "esCorrecta", source = "esCorrecta", defaultValue = "false")
    OpcionEjercicio toEntity(OpcionEjercicioDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "idOpcion", ignore = true)
    @Mapping(target = "ejercicio", ignore = true)
    void actualizar(OpcionEjercicioDto dto, @MappingTarget OpcionEjercicio entity);
}
