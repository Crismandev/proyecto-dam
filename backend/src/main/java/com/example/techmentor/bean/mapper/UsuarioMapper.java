package com.example.techmentor.bean.mapper;

import org.mapstruct.*;
import com.example.techmentor.bean.dto.UsuarioDto;
import com.example.techmentor.bean.dto.UsuarioPerfilDto;
import com.example.techmentor.bean.entity.Usuario;

/** Mapper MapStruct: Usuario -> UsuarioDto y aplicación de cambios de perfil sobre la entidad. */
@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    @Mapping(source = "rol.idRol", target = "idRol")
    @Mapping(source = "rol.nombre", target = "nombreRol")
    @Mapping(source = "estadoUsuario.idEstadoUsuario", target = "idEstadoUsuario")
    @Mapping(source = "estadoUsuario.nombre", target = "nombreEstado")
    UsuarioDto toDto(Usuario entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void actualizarPerfil(UsuarioPerfilDto dto, @MappingTarget Usuario entity);
}
