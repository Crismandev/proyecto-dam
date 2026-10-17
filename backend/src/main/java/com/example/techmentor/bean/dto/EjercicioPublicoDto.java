package com.example.techmentor.bean.dto;

import java.io.Serializable;
import java.util.List;
import lombok.*;

/** Ejercicio visto por el alumno: sin respuesta correcta ni explicación. */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EjercicioPublicoDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idEjercicio;
    private String tipoEjercicio;
    private String enunciado;
    private Integer orden;
    private Integer xpRecompensa;
    private List<OpcionEjercicioPublicaDto> opciones;
}
