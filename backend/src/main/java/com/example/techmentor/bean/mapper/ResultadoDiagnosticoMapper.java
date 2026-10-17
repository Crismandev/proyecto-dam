package com.example.techmentor.bean.mapper;

import org.mapstruct.*;
import com.example.techmentor.bean.dto.*;
import com.example.techmentor.bean.entity.*;

/** Mapper MapStruct: convierte resultado diagnostico <-> DTO. */
@Mapper(componentModel = "spring")
public interface ResultadoDiagnosticoMapper {

    @Mapping(source = "usuario.idUsuario", target = "idUsuario")
    @Mapping(source = "examen.idExamen", target = "idExamen")
    @Mapping(source = "nivelAsignado.idNivel", target = "idNivelAsignado")
    @Mapping(source = "nivelAsignado.nombre", target = "nombreNivelAsignado")
    @Mapping(target = "porcentaje", expression = "java(calcularPorcentaje(entity))")
    ResultadoDiagnosticoDto toDto(ResultadoDiagnostico entity);

    default Double calcularPorcentaje(ResultadoDiagnostico entity) {
        if (entity.getPuntajeMaximo() == null || entity.getPuntajeMaximo() == 0) {
            return 0.0;
        }
        return Math.round(entity.getPuntajeObtenido() * 10000.0 / entity.getPuntajeMaximo()) / 100.0;
    }
}
