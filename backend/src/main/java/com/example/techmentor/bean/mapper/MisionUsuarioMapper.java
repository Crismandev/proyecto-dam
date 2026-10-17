package com.example.techmentor.bean.mapper;

import org.mapstruct.*;
import com.example.techmentor.bean.dto.MisionUsuarioDto;
import com.example.techmentor.bean.entity.MisionUsuario;

/** Mapper MapStruct: MisionUsuario -> MisionUsuarioDto. */
@Mapper(componentModel = "spring")
public interface MisionUsuarioMapper {

    @Mapping(source = "usuario.idUsuario", target = "idUsuario")
    @Mapping(source = "mision.idMision", target = "idMision")
    @Mapping(source = "mision.nombre", target = "nombreMision")
    @Mapping(source = "mision.meta", target = "meta")
    MisionUsuarioDto toDto(MisionUsuario entity);
}
