package com.example.techmentor.bean.dto;

import java.io.Serializable;
import java.time.OffsetDateTime;
import lombok.*;

/** DTO de una recompensa obtenida por un usuario. */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecompensaUsuarioDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idRecompensaUsuario;
    private Long idUsuario;
    private Long idRecompensa;
    private String nombreRecompensa;
    private String tipo;
    private Integer cantidad;
    private OffsetDateTime fechaObtencion;
    private Boolean reclamada;
}
