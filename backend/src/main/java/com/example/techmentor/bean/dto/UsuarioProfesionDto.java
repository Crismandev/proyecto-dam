package com.example.techmentor.bean.dto;

import java.io.Serializable;
import java.time.OffsetDateTime;
import lombok.*;

/** DTO de una profesión (puesto) asignada a un usuario. */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioProfesionDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idUsuarioProfesion;
    private Long idUsuario;
    private Long idProfesion;
    private String nombreProfesion;
    private Boolean principal;
    private OffsetDateTime fechaAsignacion;
}
