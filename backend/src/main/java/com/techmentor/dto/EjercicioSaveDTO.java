package com.techmentor.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class EjercicioSaveDTO {

    @NotNull(message = "La lección es obligatoria")
    private Long idLeccion;

    @NotBlank(message = "El tipo de ejercicio es obligatorio")
    private String tipoEjercicio;

    @NotBlank(message = "El enunciado es obligatorio")
    private String enunciado;

    @NotNull(message = "El orden es obligatorio")
    private Integer orden;

    private String respuestaCorrecta;
    private String explicacion;

    private List<OpcionEjercicioDTO> opciones = new ArrayList<>();
}
