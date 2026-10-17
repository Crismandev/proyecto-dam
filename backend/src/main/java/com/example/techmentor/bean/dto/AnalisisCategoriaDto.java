package com.example.techmentor.bean.dto;

import java.io.Serializable;
import lombok.*;

/** Puntaje de un examen diagnóstico agrupado por categoría de pregunta. */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnalisisCategoriaDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idCategoriaPregunta;
    private String categoria;
    private Integer puntajeObtenido;
    private Integer puntajeMaximo;
    /** 0 - 100, con 2 decimales. */
    private Double porcentaje;
}
