package com.example.techmentor.bean.mapper;

import org.mapstruct.*;
import com.example.techmentor.bean.dto.RecursoLeccionDto;
import com.example.techmentor.bean.entity.RecursoLeccion;

/** Mapper MapStruct: convierte RecursoLeccion <-> RecursoLeccionDto (las relaciones se resuelven en el Model). */
@Mapper(componentModel = "spring")
public interface RecursoLeccionMapper {

    @Mapping(source = "leccion.idLeccion", target = "idLeccion")
    RecursoLeccionDto toDto(RecursoLeccion entity);

    @Mapping(target = "idRecurso", ignore = true)
    @Mapping(target = "leccion", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "activo", source = "activo", defaultValue = "true")
    RecursoLeccion toEntity(RecursoLeccionDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "idRecurso", ignore = true)
    @Mapping(target = "leccion", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    void actualizar(RecursoLeccionDto dto, @MappingTarget RecursoLeccion entity);
}
