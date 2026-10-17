package com.example.techmentor.bean.mapper;

import org.mapstruct.*;
import com.example.techmentor.bean.dto.*;
import com.example.techmentor.bean.entity.*;

/** Mapper MapStruct: convierte respuesta diagnostico <-> DTO. */
@Mapper(componentModel = "spring")
public interface RespuestaDiagnosticoMapper {

    @Mapping(source = "resultado.idResultado", target = "idResultado")
    @Mapping(source = "pregunta.idPregunta", target = "idPregunta")
    @Mapping(source = "opcionSeleccionada.idOpcionDiagnostico", target = "idOpcionSeleccionada")
    @Mapping(source = "opcionSeleccionada.esCorrecta", target = "esCorrecta")
    RespuestaDiagnosticoDto toDto(RespuestaDiagnostico entity);
}
