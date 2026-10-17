package com.example.techmentor.bean.mapper;

import org.mapstruct.*;
import com.example.techmentor.bean.dto.EstadoUsuarioDto;
import com.example.techmentor.bean.entity.EstadoUsuario;

/** Mapper MapStruct: convierte EstadoUsuario <-> EstadoUsuarioDto (las relaciones se resuelven en el Model). */
@Mapper(componentModel = "spring")
public interface EstadoUsuarioMapper {

    EstadoUsuarioDto toDto(EstadoUsuario entity);

    @Mapping(target = "idEstadoUsuario", ignore = true)
    EstadoUsuario toEntity(EstadoUsuarioDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "idEstadoUsuario", ignore = true)
    void actualizar(EstadoUsuarioDto dto, @MappingTarget EstadoUsuario entity);
}
