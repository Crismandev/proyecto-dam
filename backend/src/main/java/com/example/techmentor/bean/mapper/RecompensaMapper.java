package com.example.techmentor.bean.mapper;

import org.mapstruct.*;
import com.example.techmentor.bean.dto.RecompensaDto;
import com.example.techmentor.bean.entity.Recompensa;

/** Mapper MapStruct: convierte Recompensa <-> RecompensaDto (las relaciones se resuelven en el Model). */
@Mapper(componentModel = "spring")
public interface RecompensaMapper {

    RecompensaDto toDto(Recompensa entity);

    @Mapping(target = "idRecompensa", ignore = true)
    @Mapping(target = "cantidad", source = "cantidad", defaultValue = "0")
    @Mapping(target = "costoMonedas", source = "costoMonedas", defaultValue = "0")
    @Mapping(target = "activa", source = "activa", defaultValue = "true")
    Recompensa toEntity(RecompensaDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "idRecompensa", ignore = true)
    void actualizar(RecompensaDto dto, @MappingTarget Recompensa entity);
}
