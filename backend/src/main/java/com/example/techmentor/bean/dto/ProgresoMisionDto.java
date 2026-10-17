package com.example.techmentor.bean.dto;

import java.io.Serializable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/** DTO para fijar el progreso de una misión (lo hace el staff o el sistema, no el alumno). */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProgresoMisionDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Min(0)
    private Integer progresoActual;
}
