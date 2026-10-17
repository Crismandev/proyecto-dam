package com.example.techmentor.bean.dto;

import java.io.Serializable;
import java.util.List;
import lombok.*;

/** Nivel de un curso con sus lecciones. */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NivelContenidoDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idNivel;
    private String nombre;
    private String descripcion;
    private Integer orden;
    private List<LeccionContenidoDto> lecciones;
}
