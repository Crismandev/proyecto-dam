package com.example.techmentor.bean.dto;

import java.io.Serializable;
import jakarta.validation.constraints.*;
import lombok.*;

/** DTO de profesión: datos que viajan entre la API y el cliente. */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfesionDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idProfesion;

    @NotBlank
    @Size(max = 100)
    private String nombre;

    private String descripcion;

    private Boolean activo;

}
