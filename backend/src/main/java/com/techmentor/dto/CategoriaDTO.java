package com.techmentor.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaDTO {
    private Long idCategoria;

    @NotBlank(message = "El nombre de categoría es obligatorio")
    private String nombreCategoria;

    private String descripcion;
    private String icono;
}
