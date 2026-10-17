package com.example.techmentor.bean.mapper;

import org.mapstruct.*;
import com.example.techmentor.bean.dto.RolDto;
import com.example.techmentor.bean.entity.Rol;

/** Mapper MapStruct: convierte Rol <-> RolDto (las relaciones se resuelven en el Model). */
@Mapper(componentModel = "spring")
public interface RolMapper {

    RolDto toDto(Rol entity);

    @Mapping(target = "idRol", ignore = true)
    Rol toEntity(RolDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "idRol", ignore = true)
    void actualizar(RolDto dto, @MappingTarget Rol entity);
}
