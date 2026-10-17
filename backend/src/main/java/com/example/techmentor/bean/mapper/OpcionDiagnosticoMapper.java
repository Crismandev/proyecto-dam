package com.example.techmentor.bean.mapper;

import org.mapstruct.*;
import com.example.techmentor.bean.dto.*;
import com.example.techmentor.bean.entity.*;

/** Mapper MapStruct: convierte opcion diagnostico <-> DTO. */
@Mapper(componentModel = "spring")
public interface OpcionDiagnosticoMapper {

    @Mapping(source = "pregunta.idPregunta", target = "idPregunta")
    OpcionDiagnosticoDto toDto(OpcionDiagnostico entity);

    OpcionPublicaDto toPublicDto(OpcionDiagnostico entity);

    @Mapping(target = "idOpcionDiagnostico", ignore = true)
    @Mapping(target = "pregunta", ignore = true)
    @Mapping(target = "esCorrecta", source = "esCorrecta", defaultValue = "false")
    OpcionDiagnostico toEntity(OpcionDiagnosticoDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "idOpcionDiagnostico", ignore = true)
    @Mapping(target = "pregunta", ignore = true)
    void actualizar(OpcionDiagnosticoDto dto, @MappingTarget OpcionDiagnostico entity);
}
