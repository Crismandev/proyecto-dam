package com.example.techmentor.bean.dto;

import java.io.Serializable;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/** DTO para asignarse una profesión; la primera profesión del usuario queda como principal. */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AsignarProfesionDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    private Long idProfesion;

    /** true = convertirla en la profesión principal (puesto al que postula). */
    private Boolean principal;
}
