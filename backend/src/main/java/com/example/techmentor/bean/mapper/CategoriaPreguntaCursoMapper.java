package com.example.techmentor.bean.mapper;

import org.mapstruct.*;
import com.example.techmentor.bean.dto.CategoriaPreguntaCursoDto;
import com.example.techmentor.bean.entity.CategoriaPreguntaCurso;

/** Mapper MapStruct: convierte CategoriaPreguntaCurso <-> CategoriaPreguntaCursoDto (las relaciones se resuelven en el Model). */
@Mapper(componentModel = "spring")
public interface CategoriaPreguntaCursoMapper {

    @Mapping(source = "categoriaPregunta.idCategoriaPregunta", target = "idCategoriaPregunta")
    @Mapping(source = "curso.idCurso", target = "idCurso")
    CategoriaPreguntaCursoDto toDto(CategoriaPreguntaCurso entity);

    @Mapping(target = "idCategoriaPreguntaCurso", ignore = true)
    @Mapping(target = "categoriaPregunta", ignore = true)
    @Mapping(target = "curso", ignore = true)
    @Mapping(target = "umbralPorcentaje", source = "umbralPorcentaje", defaultValue = "60")
    CategoriaPreguntaCurso toEntity(CategoriaPreguntaCursoDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "idCategoriaPreguntaCurso", ignore = true)
    @Mapping(target = "categoriaPregunta", ignore = true)
    @Mapping(target = "curso", ignore = true)
    void actualizar(CategoriaPreguntaCursoDto dto, @MappingTarget CategoriaPreguntaCurso entity);
}
