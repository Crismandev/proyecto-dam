package com.example.techmentor.bean.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.example.techmentor.bean.dto.ProgresoLeccionDto;
import com.example.techmentor.bean.entity.ProgresoLeccion;

/** Mapper MapStruct: convierte progreso leccion <-> DTO. */
@Mapper(componentModel = "spring")
public interface ProgresoLeccionMapper {

    @Mapping(source = "usuario.idUsuario", target = "idUsuario")
    @Mapping(source = "leccion.idLeccion", target = "idLeccion")
    @Mapping(source = "leccion.titulo", target = "tituloLeccion")
    ProgresoLeccionDto toDto(ProgresoLeccion entity);
}
