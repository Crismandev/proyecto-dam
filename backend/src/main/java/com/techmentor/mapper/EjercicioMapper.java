package com.techmentor.mapper;

import com.techmentor.dto.EjercicioDTO;
import com.techmentor.dto.OpcionEjercicioDTO;
import com.techmentor.entity.Ejercicio;
import com.techmentor.entity.OpcionEjercicio;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.stream.Collectors;

@Component
public class EjercicioMapper {

    public OpcionEjercicioDTO toOpcionDTO(OpcionEjercicio entity) {
        if (entity == null) return null;
        return OpcionEjercicioDTO.builder()
                .idOpcion(entity.getIdOpcion())
                .texto(entity.getTexto())
                .esCorrecta(entity.getEsCorrecta())
                .orden(entity.getOrden())
                .build();
    }

    public EjercicioDTO toDTO(Ejercicio entity) {
        if (entity == null) return null;

        var opcionesDTO = entity.getOpciones() != null ?
                entity.getOpciones().stream().map(this::toOpcionDTO).collect(Collectors.toList()) :
                Collections.<OpcionEjercicioDTO>emptyList();

        return EjercicioDTO.builder()
                .idEjercicio(entity.getIdEjercicio())
                .idLeccion(entity.getLeccion() != null ? entity.getLeccion().getIdLeccion() : null)
                .tituloLeccion(entity.getLeccion() != null ? entity.getLeccion().getTitulo() : null)
                .tipoEjercicio(entity.getTipoEjercicio())
                .enunciado(entity.getEnunciado())
                .orden(entity.getOrden())
                .respuestaCorrecta(entity.getRespuestaCorrecta())
                .explicacion(entity.getExplicacion())
                .opciones(opcionesDTO)
                .build();
    }
}
