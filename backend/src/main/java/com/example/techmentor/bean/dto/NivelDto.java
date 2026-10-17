package com.example.techmentor.bean.dto;

import java.io.Serializable;
import jakarta.validation.constraints.*;
import lombok.*;

/** DTO de nivel: datos que viajan entre la API y el cliente. */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NivelDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idNivel;

    @NotNull
    private Long idCurso;

    @NotBlank
    @Size(max = 100)
    private String nombre;

    private String descripcion;

    @NotNull
    @Min(1)
    private Integer orden;

}
