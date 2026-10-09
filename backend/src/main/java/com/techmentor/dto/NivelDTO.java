package com.techmentor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NivelDTO {
    private Long idNivel;
    private Long idCurso;
    private String nombreCurso;
    private String nombreNivel;
    private Integer orden;
    private String descripcion;
}
