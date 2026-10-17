package com.example.techmentor.bean.dto;

import java.io.Serializable;
import lombok.*;
import java.time.OffsetDateTime;

/** DTO de resultado diagnostico: datos que viajan entre la API y el cliente. */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResultadoDiagnosticoDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idResultado;
    private Long idUsuario;
    private Long idExamen;
    private Long idNivelAsignado;
    private String nombreNivelAsignado;
    private Integer puntajeObtenido;
    private Integer puntajeMaximo;
    /** 0 - 100, con 2 decimales. */
    private Double porcentaje;
    private OffsetDateTime fechaRealizacion;
}
