package com.example.techmentor.bean.dto;

import java.io.Serializable;
import java.time.OffsetDateTime;
import lombok.*;

/** DTO de un curso recomendado: incluye el motivo (reglas) y la explicación de IA si está habilitada. */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecomendacionCursoDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idRecomendacion;
    private Long idUsuario;
    private Long idResultado;
    private Long idCurso;
    private String nombreCurso;
    private Long idProfesion;
    private String nombreProfesion;
    /** 1 = alta, 2 = media, 3 = baja. */
    private Integer prioridad;
    private String motivo;
    private String origen;
    private String explicacionIa;
    private String estado;
    private OffsetDateTime fechaCreacion;
}
