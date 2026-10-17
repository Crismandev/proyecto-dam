package com.example.techmentor.bean.dto;

import java.io.Serializable;
import jakarta.validation.constraints.*;
import lombok.*;

/** DTO de relación puesto-curso: datos que viajan entre la API y el cliente. */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfesionCursoDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idProfesionCurso;

    @NotNull
    private Long idProfesion;

    @NotNull
    private Long idCurso;

    @Min(1)
    @Max(3)
    private Integer prioridad;

    private Boolean obligatorio;

}
