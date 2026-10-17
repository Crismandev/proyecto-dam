package com.example.techmentor.bean.dto;

import java.io.Serializable;
import lombok.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/** Vista para ADMINISTRADOR / MAESTRO (incluye esCorrecta). */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OpcionDiagnosticoDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idOpcionDiagnostico;

    @NotNull
    private Long idPregunta;

    @NotBlank
    private String texto;

    /** Si no se envía al crear, queda en false. */
    private Boolean esCorrecta;

    @Min(1)
    private Integer orden;
}
