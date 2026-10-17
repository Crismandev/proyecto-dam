package com.example.techmentor.bean.dto;

import java.io.Serializable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

/** Respuesta del alumno: opción seleccionada o texto, según el ejercicio. */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RespuestaLeccionDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Positive
    private Long idEjercicio;

    @Positive
    private Long idOpcionSeleccionada;

    /** Para respuestas escritas se compara el texto exacto, sin espacios laterales. */
    private String respuestaTexto;
}
