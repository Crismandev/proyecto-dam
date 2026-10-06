package com.techmentor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MobileCursoDTO {
    private Long idCurso;
    private String nombreCurso;
    private String descripcion;
    private String icono;
    private String dificultad;
    private Integer xpRequerido;
    private Integer xpTotalDisponible;
    private Integer porcentajeProgreso;
    private Boolean bloqueado;
}
