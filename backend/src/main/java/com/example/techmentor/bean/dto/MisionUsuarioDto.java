package com.example.techmentor.bean.dto;

import java.io.Serializable;
import java.time.OffsetDateTime;
import lombok.*;

/** DTO del avance de un usuario en una misión. */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MisionUsuarioDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idMisionUsuario;
    private Long idUsuario;
    private Long idMision;
    private String nombreMision;
    private Integer meta;
    private Integer progresoActual;
    private Boolean completada;
    private OffsetDateTime fechaAsignacion;
    private OffsetDateTime fechaCompletada;
}
