package com.example.techmentor.bean.mapper;

import org.mapstruct.*;
import com.example.techmentor.bean.dto.*;
import com.example.techmentor.bean.entity.*;

/** Mapper MapStruct: convierte pregunta diagnostico <-> DTO. */
@Mapper(componentModel = "spring")
public interface PreguntaDiagnosticoMapper {

    @Mapping(source = "examen.idExamen", target = "idExamen")
    @Mapping(source = "categoriaPregunta.idCategoriaPregunta", target = "idCategoriaPregunta")
    PreguntaDiagnosticoDto toDto(PreguntaDiagnostico entity);

    @Mapping(target = "idPregunta", ignore = true)
    @Mapping(target = "examen", ignore = true)
    @Mapping(target = "categoriaPregunta", ignore = true)
    @Mapping(target = "puntaje", source = "puntaje", defaultValue = "1")
    PreguntaDiagnostico toEntity(PreguntaDiagnosticoDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "idPregunta", ignore = true)
    @Mapping(target = "examen", ignore = true)
    @Mapping(target = "categoriaPregunta", ignore = true)
    void actualizar(PreguntaDiagnosticoDto dto, @MappingTarget PreguntaDiagnostico entity);

    /** Las opciones se cargan aparte en el Model. */
    @Mapping(source = "categoriaPregunta.idCategoriaPregunta", target = "idCategoriaPregunta")
    @Mapping(target = "opciones", ignore = true)
    PreguntaPublicaDto toPublicDto(PreguntaDiagnostico entity);
}
