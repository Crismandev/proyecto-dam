package com.example.techmentor.bean.mapper;

import org.mapstruct.*;
import com.example.techmentor.bean.dto.UsuarioProfesionDto;
import com.example.techmentor.bean.entity.UsuarioProfesion;

/** Mapper MapStruct: UsuarioProfesion -> UsuarioProfesionDto. */
@Mapper(componentModel = "spring")
public interface UsuarioProfesionMapper {

    @Mapping(source = "usuario.idUsuario", target = "idUsuario")
    @Mapping(source = "profesion.idProfesion", target = "idProfesion")
    @Mapping(source = "profesion.nombre", target = "nombreProfesion")
    UsuarioProfesionDto toDto(UsuarioProfesion entity);
}
