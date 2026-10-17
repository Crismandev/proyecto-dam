package com.example.techmentor.bean.dto;

import java.io.Serializable;
import lombok.*;
import java.util.List;

/** DTO de pregunta publica: datos que viajan entre la API y el cliente. */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PreguntaPublicaDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idPregunta;
    private Long idCategoriaPregunta;
    private String enunciado;
    private String nivelDificultad;
    private String tipoPregunta;
    private Integer puntaje;
    private Integer orden;
    private List<OpcionPublicaDto> opciones;
}
