package com.example.techmentor.bean.dto;

import java.io.Serializable;
import lombok.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** DTO de categoria pregunta: datos que viajan entre la API y el cliente. */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoriaPreguntaDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idCategoriaPregunta;

    @NotBlank
    @Size(max = 100)
    private String nombre;

    private String descripcion;
}
