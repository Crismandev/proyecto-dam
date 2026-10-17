package com.example.techmentor.bean.dto;

import java.io.Serializable;
import jakarta.validation.constraints.*;
import lombok.*;

/** DTO de ejercicio: datos que viajan entre la API y el cliente. */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EjercicioDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idEjercicio;

    @NotNull
    private Long idLeccion;

    @NotBlank
    @Size(max = 50)
    private String tipoEjercicio;

    @NotBlank
    private String enunciado;

    @NotNull
    @Min(1)
    private Integer orden;

    private String respuestaCorrecta;

    private String explicacion;

    @Min(0)
    private Integer xpRecompensa;

}
