package com.techmentor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeccionDTO {
    private Long idLeccion;
    private Long idNivel;
    private String nombreNivel;
    private Long idCurso;
    private String nombreCurso;
    private String titulo;
    private Integer orden;
    private String descripcion;
}
