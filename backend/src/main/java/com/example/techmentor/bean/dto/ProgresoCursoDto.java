package com.example.techmentor.bean.dto;

import java.io.Serializable;
import java.time.OffsetDateTime;
import lombok.*;

/** DTO de progreso curso: datos que viajan entre la API y el cliente. */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProgresoCursoDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idProgreso;
    private Long idUsuario;
    private Long idCurso;
    private String nombreCurso;
    private Long idNivelActual;
    private String nombreNivelActual;
    private OffsetDateTime fechaInicio;
    private OffsetDateTime fechaUltimaActividad;
    /** NO_INICIADO, EN_CURSO, COMPLETADO o ABANDONADO. */
    private String estado;
    private Integer xpGanado;
    private OffsetDateTime fechaCompletado;
}
