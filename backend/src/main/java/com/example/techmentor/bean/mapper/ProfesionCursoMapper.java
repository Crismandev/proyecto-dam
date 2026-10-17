package com.example.techmentor.bean.mapper;

import org.mapstruct.*;
import com.example.techmentor.bean.dto.ProfesionCursoDto;
import com.example.techmentor.bean.entity.ProfesionCurso;

/** Mapper MapStruct: convierte ProfesionCurso <-> ProfesionCursoDto (las relaciones se resuelven en el Model). */
@Mapper(componentModel = "spring")
public interface ProfesionCursoMapper {

    @Mapping(source = "profesion.idProfesion", target = "idProfesion")
    @Mapping(source = "curso.idCurso", target = "idCurso")
    ProfesionCursoDto toDto(ProfesionCurso entity);

    @Mapping(target = "idProfesionCurso", ignore = true)
    @Mapping(target = "profesion", ignore = true)
    @Mapping(target = "curso", ignore = true)
    @Mapping(target = "prioridad", source = "prioridad", defaultValue = "2")
    @Mapping(target = "obligatorio", source = "obligatorio", defaultValue = "true")
    ProfesionCurso toEntity(ProfesionCursoDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "idProfesionCurso", ignore = true)
    @Mapping(target = "profesion", ignore = true)
    @Mapping(target = "curso", ignore = true)
    void actualizar(ProfesionCursoDto dto, @MappingTarget ProfesionCurso entity);
}
