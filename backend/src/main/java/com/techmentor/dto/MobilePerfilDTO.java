package com.techmentor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MobilePerfilDTO {
    private Long idUsuario;
    private String nombres;
    private String apellidos;
    private String correo;
    private Long experienciaTotal;
    private Integer rachaDias;
    private Integer nivelActual; // Calculado en base a experiencia
    private String rango; // Ej. "Principiante", "Novato", etc.
}
