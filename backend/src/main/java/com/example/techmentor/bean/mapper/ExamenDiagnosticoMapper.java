package com.example.techmentor.bean.mapper;

import org.mapstruct.*;
import com.example.techmentor.bean.dto.*;
import com.example.techmentor.bean.entity.*;

/** Mapper MapStruct: convierte examen diagnostico <-> DTO. */
@Mapper(componentModel = "spring")
public interface ExamenDiagnosticoMapper {

    @Mapping(source = "curso.idCurso", target = "idCurso")
    @Mapping(source = "profesion.idProfesion", target = "idProfesion")
    ExamenDiagnosticoDto toDto(ExamenDiagnostico entity);

    @Mapping(target = "idExamen", ignore = true)
    @Mapping(target = "curso", ignore = true)
    @Mapping(target = "profesion", ignore = true)
    @Mapping(target = "activo", source = "activo", defaultValue = "true")
    ExamenDiagnostico toEntity(ExamenDiagnosticoDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "idExamen", ignore = true)
    @Mapping(target = "curso", ignore = true)
    @Mapping(target = "profesion", ignore = true)
    void actualizar(ExamenDiagnosticoDto dto, @MappingTarget ExamenDiagnostico entity);
}
