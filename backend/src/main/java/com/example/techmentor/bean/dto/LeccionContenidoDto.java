package com.example.techmentor.bean.dto;

import java.io.Serializable;
import java.util.List;
import lombok.*;

/** Lección completa: texto, recursos (videos/textos/enlaces), ejercicios y el estado del alumno. */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LeccionContenidoDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idLeccion;
    private String titulo;
    private String descripcion;
    private String contenido;
    private Integer orden;
    private Integer xpRecompensa;
    /** NO_INICIADO, EN_CURSO o COMPLETADO (del usuario que consulta). */
    private String estado;
    private List<RecursoLeccionDto> recursos;
    private List<EjercicioPublicoDto> ejercicios;
}
