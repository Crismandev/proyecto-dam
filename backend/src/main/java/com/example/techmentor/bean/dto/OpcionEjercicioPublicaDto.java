package com.example.techmentor.bean.dto;

import java.io.Serializable;
import lombok.*;

/** Opción de un ejercicio vista por el alumno: NO incluye si es correcta. */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OpcionEjercicioPublicaDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idOpcion;
    private String texto;
    private Integer orden;
}
