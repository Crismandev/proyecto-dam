package com.techmentor.mapper;

import com.techmentor.dto.ProgresoLeccionDTO;
import com.techmentor.entity.ProgresoLeccion;
import org.springframework.stereotype.Component;

@Component
public class ProgresoLeccionMapper {

    public ProgresoLeccionDTO toDTO(ProgresoLeccion entity) {
        if (entity == null) return null;

        Long idCurso = null;
        String nombreCurso = null;
        if (entity.getLeccion() != null && entity.getLeccion().getNivel() != null && entity.getLeccion().getNivel().getCurso() != null) {
            idCurso = entity.getLeccion().getNivel().getCurso().getIdCurso();
            nombreCurso = entity.getLeccion().getNivel().getCurso().getNombre();
        }

        String nombreUsuario = null;
        if (entity.getUsuario() != null) {
            nombreUsuario = entity.getUsuario().getNombres() + (entity.getUsuario().getApellidos() != null ? " " + entity.getUsuario().getApellidos() : "");
        }

        return ProgresoLeccionDTO.builder()
                .idProgreso(entity.getIdProgreso())
                .idUsuario(entity.getUsuario() != null ? entity.getUsuario().getIdUsuario() : null)
                .nombreUsuario(nombreUsuario)
                .correoUsuario(entity.getUsuario() != null ? entity.getUsuario().getEmail() : null)
                .idLeccion(entity.getLeccion() != null ? entity.getLeccion().getIdLeccion() : null)
                .tituloLeccion(entity.getLeccion() != null ? entity.getLeccion().getTitulo() : null)
                .idCurso(idCurso)
                .nombreCurso(nombreCurso)
                .aciertos(entity.getPuntaje())
                .totalPreguntas(10)
                .xpGanado(entity.getXpGanado())
                .fechaCompletado(entity.getFechaCompletado() != null ? entity.getFechaCompletado().toLocalDateTime() : null)
                .build();
    }
}
