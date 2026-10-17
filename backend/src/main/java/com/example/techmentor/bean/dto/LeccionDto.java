package com.example.techmentor.bean.dto;

import java.io.Serializable;
import jakarta.validation.constraints.*;
import lombok.*;

/** DTO de lección: datos que viajan entre la API y el cliente. */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LeccionDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idLeccion;

    @NotNull
    private Long idNivel;

    @NotBlank
    @Size(max = 150)
    private String titulo;

    private String descripcion;

    private String contenido;

    @NotNull
    @Min(1)
    private Integer orden;

    @Min(0)
    private Integer xpRecompensa;

}
