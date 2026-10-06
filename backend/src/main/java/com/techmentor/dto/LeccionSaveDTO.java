package com.techmentor.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LeccionSaveDTO {

    @NotNull(message = "El nivel es obligatorio")
    private Long idNivel;

    @NotBlank(message = "El título de la lección es obligatorio")
    private String titulo;

    @NotNull(message = "El orden es obligatorio")
    private Integer orden;

    private String descripcion;
}
