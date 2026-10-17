package com.example.techmentor.bean.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.example.techmentor.bean.dto.ProgresoCursoDto;
import com.example.techmentor.bean.entity.ProgresoCurso;

/** Mapper MapStruct: convierte progreso curso <-> DTO. */
@Mapper(componentModel = "spring")
public interface ProgresoCursoMapper {

    @Mapping(source = "usuario.idUsuario", target = "idUsuario")
    @Mapping(source = "curso.idCurso", target = "idCurso")
    @Mapping(source = "curso.nombre", target = "nombreCurso")
    @Mapping(source = "nivelActual.idNivel", target = "idNivelActual")
    @Mapping(source = "nivelActual.nombre", target = "nombreNivelActual")
    ProgresoCursoDto toDto(ProgresoCurso entity);
}
