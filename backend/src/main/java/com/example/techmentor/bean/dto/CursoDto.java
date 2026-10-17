package com.example.techmentor.bean.dto;

import java.io.Serializable;
import java.time.OffsetDateTime;
import jakarta.validation.constraints.*;
import lombok.*;

/** DTO de curso: datos que viajan entre la API y el cliente. */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CursoDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idCurso;

    @NotNull
    private Long idCategoria;

    @NotBlank
    @Size(max = 150)
    private String nombre;

    private String descripcion;

    @Size(max = 500)
    private String icono;

    @Min(0)
    private Integer xpRequerido;

    @Min(0)
    private Integer xpRecompensa;

    private Boolean activo;

    private OffsetDateTime fechaCreacion;

    private OffsetDateTime fechaActualizacion;

}
