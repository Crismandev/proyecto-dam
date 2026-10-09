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
public class RolDTO {
    private Long idRol;

    @NotBlank(message = "El nombre del rol es obligatorio")
    private String nombreRol;

    private String descripcion;
}
