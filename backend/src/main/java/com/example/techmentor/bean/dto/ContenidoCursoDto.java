package com.example.techmentor.bean.dto;

import java.io.Serializable;
import java.util.List;
import lombok.*;

/** Estructura completa de un curso: niveles > lecciones > recursos y ejercicios. */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContenidoCursoDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idCurso;
    private String nombre;
    private String descripcion;
    private Integer xpRecompensa;
    private List<NivelContenidoDto> niveles;
}
