package com.techmentor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProgresoLeccionDTO {
    private Long idProgreso;
    private Long idUsuario;
    private String nombreUsuario;
    private String correoUsuario;
    private Long idLeccion;
    private String tituloLeccion;
    private Long idCurso;
    private String nombreCurso;
    private Integer aciertos;
    private Integer totalPreguntas;
    private Integer xpGanado;
    private LocalDateTime fechaCompletado;
    private UUID uuidCliente;
}
