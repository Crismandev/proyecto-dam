package com.example.techmentor.bean.mapper;

import org.mapstruct.*;
import com.example.techmentor.bean.dto.RecomendacionCursoDto;
import com.example.techmentor.bean.entity.RecomendacionCurso;

/** Mapper MapStruct: RecomendacionCurso -> RecomendacionCursoDto. */
@Mapper(componentModel = "spring")
public interface RecomendacionCursoMapper {

    @Mapping(source = "usuario.idUsuario", target = "idUsuario")
    @Mapping(source = "resultado.idResultado", target = "idResultado")
    @Mapping(source = "curso.idCurso", target = "idCurso")
    @Mapping(source = "curso.nombre", target = "nombreCurso")
    @Mapping(source = "profesion.idProfesion", target = "idProfesion")
    @Mapping(source = "profesion.nombre", target = "nombreProfesion")
    RecomendacionCursoDto toDto(RecomendacionCurso entity);
}
