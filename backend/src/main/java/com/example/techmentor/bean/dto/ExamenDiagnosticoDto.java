package com.example.techmentor.bean.dto;

import java.io.Serializable;
import lombok.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** DTO de examen diagnostico: datos que viajan entre la API y el cliente. */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExamenDiagnosticoDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idExamen;

    /** Opcional: null = examen global (sin curso). */
    private Long idCurso;

    /** Opcional: puesto (profesión) para el que se adapta el examen; null = examen general. */
    private Long idProfesion;

    @NotBlank
    @Size(max = 150)
    private String nombre;

    private String descripcion;

    /** Si no se envía al crear, queda en true. */
    private Boolean activo;
}
