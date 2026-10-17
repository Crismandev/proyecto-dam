package com.example.techmentor.bean.dto;

import java.io.Serializable;
import java.time.OffsetDateTime;
import jakarta.validation.constraints.*;
import lombok.*;

/** DTO de recurso de lección: datos que viajan entre la API y el cliente. */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecursoLeccionDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idRecurso;

    @NotNull
    private Long idLeccion;

    @NotBlank
    @Size(max = 20)
    @Pattern(regexp = "VIDEO|TEXTO|ENLACE")
    private String tipo;

    @NotBlank
    @Size(max = 200)
    private String titulo;

    @Size(max = 1000)
    private String url;

    private String contenido;

    @Size(max = 30)
    @Pattern(regexp = "YOUTUBE|VIMEO|OTRO")
    private String plataforma;

    @Min(0)
    private Integer duracionSegundos;

    @NotNull
    @Min(1)
    private Integer orden;

    private Boolean activo;

    private OffsetDateTime fechaCreacion;

}
