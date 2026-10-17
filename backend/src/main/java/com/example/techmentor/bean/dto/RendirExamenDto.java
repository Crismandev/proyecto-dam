package com.example.techmentor.bean.dto;

import java.io.Serializable;
import lombok.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/** DTO de rendir examen: datos que viajan entre la API y el cliente. */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RendirExamenDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Valid
    private List<RespuestaEnvioDto> respuestas;
}
