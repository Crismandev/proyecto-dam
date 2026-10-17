package com.example.techmentor.bean.dto;

import java.io.Serializable;
import lombok.*;
import jakarta.validation.constraints.NotNull;

/** DTO de resultado nivel: datos que viajan entre la API y el cliente. */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResultadoNivelDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    private Long idNivelAsignado;
}
