package com.example.techmentor.bean.mapper;

import org.mapstruct.*;
import com.example.techmentor.bean.dto.HistorialEstadoDto;
import com.example.techmentor.bean.entity.HistorialEstadoUsuario;

/** Mapper MapStruct: HistorialEstadoUsuario -> HistorialEstadoDto. */
@Mapper(componentModel = "spring")
public interface HistorialEstadoMapper {

    @Mapping(source = "usuario.idUsuario", target = "idUsuario")
    @Mapping(source = "estadoUsuario.idEstadoUsuario", target = "idEstadoUsuario")
    @Mapping(source = "estadoUsuario.nombre", target = "nombreEstado")
    HistorialEstadoDto toDto(HistorialEstadoUsuario entity);
}
