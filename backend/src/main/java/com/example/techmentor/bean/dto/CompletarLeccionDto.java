package com.example.techmentor.bean.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import lombok.*;

/** DTO de completar leccion: datos que viajan entre la API y el cliente. */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompletarLeccionDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Min(0)
    private Integer puntaje;
}
