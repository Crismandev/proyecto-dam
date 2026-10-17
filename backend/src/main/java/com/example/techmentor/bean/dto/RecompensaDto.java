package com.example.techmentor.bean.dto;

import java.io.Serializable;
import jakarta.validation.constraints.*;
import lombok.*;

/** DTO de recompensa: datos que viajan entre la API y el cliente. */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecompensaDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idRecompensa;

    @NotBlank
    @Size(max = 150)
    private String nombre;

    private String descripcion;

    @NotBlank
    @Size(max = 50)
    private String tipo;

    @Min(0)
    private Integer cantidad;

    @Min(0)
    private Integer costoMonedas;

    @Min(1)
    private Integer diaSecuenciaRequerido;

    private Boolean activa;

}
