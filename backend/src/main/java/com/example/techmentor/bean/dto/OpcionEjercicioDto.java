package com.example.techmentor.bean.dto;

import java.io.Serializable;
import jakarta.validation.constraints.*;
import lombok.*;

/** DTO de opción de ejercicio: datos que viajan entre la API y el cliente. */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OpcionEjercicioDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idOpcion;

    @NotNull
    private Long idEjercicio;

    @NotBlank
    private String texto;

    private Boolean esCorrecta;

    @Min(1)
    private Integer orden;

}
