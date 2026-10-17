package com.example.techmentor.bean.dto;

import java.io.Serializable;
import java.time.OffsetDateTime;
import lombok.*;

/** DTO de una fila del historial de estados de un usuario. */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HistorialEstadoDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idHistorial;
    private Long idUsuario;
    private Long idEstadoUsuario;
    private String nombreEstado;
    private String motivo;
    private OffsetDateTime fechaCambio;
}
