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
public class MobileEvaluacionDTO {
    private Long idEjercicio;
    private Long idOpcionSeleccionada;
    private Boolean esCorrecta;
    private String explicacion;
    private String respuestaCorrectaTexto;
}
