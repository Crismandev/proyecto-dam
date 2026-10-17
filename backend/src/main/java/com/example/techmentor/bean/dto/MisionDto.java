package com.example.techmentor.bean.dto;

import java.io.Serializable;
import java.time.OffsetDateTime;
import jakarta.validation.constraints.*;
import lombok.*;

/** DTO de misión: datos que viajan entre la API y el cliente. */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MisionDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idMision;

    @NotBlank
    @Size(max = 150)
    private String nombre;

    private String descripcion;

    @NotBlank
    @Size(max = 50)
    private String tipo;

    @NotNull
    @Min(1)
    private Integer meta;

    @Min(0)
    private Integer xpRecompensa;

    private OffsetDateTime fechaInicio;

    private OffsetDateTime fechaFin;

    private Boolean activa;

}
