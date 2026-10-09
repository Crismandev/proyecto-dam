package com.techmentor.dto;

import com.techmentor.entity.Dificultad;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CursoSaveDTO {

    @NotNull(message = "La categoría es obligatoria")
    private Long idCategoria;

    @NotBlank(message = "El nombre del curso es obligatorio")
    private String nombreCurso;

    private String descripcion;
    private String icono;

    @NotNull(message = "La dificultad es obligatoria")
    private Dificultad dificultad;

    private Integer xpRequerido = 0;
    private Integer xpRecompensa = 0;
}
