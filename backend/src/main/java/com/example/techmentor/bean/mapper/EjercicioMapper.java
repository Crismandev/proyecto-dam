package com.example.techmentor.bean.mapper;

import org.mapstruct.*;
import com.example.techmentor.bean.dto.EjercicioDto;
import com.example.techmentor.bean.entity.Ejercicio;

/** Mapper MapStruct: convierte Ejercicio <-> EjercicioDto (las relaciones se resuelven en el Model). */
@Mapper(componentModel = "spring")
public interface EjercicioMapper {

    @Mapping(source = "leccion.idLeccion", target = "idLeccion")
    EjercicioDto toDto(Ejercicio entity);

    @Mapping(target = "idEjercicio", ignore = true)
    @Mapping(target = "leccion", ignore = true)
    @Mapping(target = "xpRecompensa", source = "xpRecompensa", defaultValue = "0")
    Ejercicio toEntity(EjercicioDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "idEjercicio", ignore = true)
    @Mapping(target = "leccion", ignore = true)
    void actualizar(EjercicioDto dto, @MappingTarget Ejercicio entity);
}
