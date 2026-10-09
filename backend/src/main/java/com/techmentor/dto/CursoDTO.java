package com.techmentor.dto;

import com.techmentor.entity.Dificultad;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CursoDTO {
    private Long idCurso;
    private Long idCategoria;
    private String nombreCategoria;
    private String nombreCurso;
    private String descripcion;
    private String icono;
    private Dificultad dificultad;
    private Integer xpRequerido;
    private Integer xpRecompensa;
}
