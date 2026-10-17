package com.example.techmentor.bean.dto;

import java.io.Serializable;
import lombok.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/** DTO de pregunta diagnostico: datos que viajan entre la API y el cliente. */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PreguntaDiagnosticoDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idPregunta;

    @NotNull
    private Long idExamen;

    @NotNull
    private Long idCategoriaPregunta;

    @NotBlank
    private String enunciado;

    @Size(max = 30)
    private String nivelDificultad;

    /** Ej.: OPCION_MULTIPLE, VERDADERO_FALSO. */
    @NotBlank
    @Size(max = 50)
    private String tipoPregunta;

    /** Si no se envía al crear, queda en 1. */
    @Min(1)
    private Integer puntaje;

    @NotNull
    @Min(1)
    private Integer orden;
}
