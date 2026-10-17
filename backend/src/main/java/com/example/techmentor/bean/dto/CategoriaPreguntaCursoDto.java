package com.example.techmentor.bean.dto;

import java.io.Serializable;
import jakarta.validation.constraints.*;
import lombok.*;

/** DTO de relación categoría de pregunta-curso: datos que viajan entre la API y el cliente. */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoriaPreguntaCursoDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idCategoriaPreguntaCurso;

    @NotNull
    private Long idCategoriaPregunta;

    @NotNull
    private Long idCurso;

    @Min(0)
    @Max(100)
    private Integer umbralPorcentaje;

}
