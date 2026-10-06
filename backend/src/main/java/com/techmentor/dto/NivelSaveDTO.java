package com.techmentor.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NivelSaveDTO {

    @NotNull(message = "El curso es obligatorio")
    private Long idCurso;

    @NotBlank(message = "El nombre del nivel es obligatorio")
    private String nombreNivel;

    @NotNull(message = "El orden es obligatorio")
    private Integer orden;

    private String descripcion;
}
