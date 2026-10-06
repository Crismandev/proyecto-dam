package com.techmentor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EjercicioDTO {
    private Long idEjercicio;
    private Long idLeccion;
    private String tituloLeccion;
    private String tipoEjercicio;
    private String enunciado;
    private Integer orden;
    private String respuestaCorrecta;
    private String explicacion;
    private List<OpcionEjercicioDTO> opciones;
}
