package com.example.techmentor.bean.dto;

import java.io.Serializable;
import lombok.*;

/** DTO de respuesta diagnostico: datos que viajan entre la API y el cliente. */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RespuestaDiagnosticoDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idRespuesta;
    private Long idResultado;
    private Long idPregunta;
    private Long idOpcionSeleccionada;
    /** null si la pregunta quedó sin responder. */
    private Boolean esCorrecta;
}
