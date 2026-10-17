package com.example.techmentor.bean.dto;

import java.io.Serializable;
import java.time.OffsetDateTime;
import lombok.*;

/** DTO de progreso leccion: datos que viajan entre la API y el cliente. */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProgresoLeccionDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idProgresoLeccion;
    private Long idUsuario;
    private Long idLeccion;
    private String tituloLeccion;
    /** NO_INICIADO, EN_CURSO o COMPLETADO. */
    private String estado;
    private Integer puntaje;
    private Integer intentos;
    private OffsetDateTime fechaInicio;
    private OffsetDateTime fechaUltimaActividad;
    private OffsetDateTime fechaCompletado;
    private Integer xpGanado;
}
