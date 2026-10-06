package com.techmentor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlumnoDetalleDTO {
    private Long idUsuario;
    private String nombres;
    private String apellidos;
    private String correo;
    private String rol;
    private Integer xpTotal;
    private Integer monedas;
    private LocalDateTime fechaRegistro;
    private LocalDate fechaNacimiento;
    private String avatar;

    private List<ProgresoAlumnoDTO> progreso;
    private List<CursoAlumnoDTO> cursos;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProgresoAlumnoDTO {
        private Long idProgreso;
        private String curso;
        private String leccion;
        private Integer aciertos;
        private Integer totalPreguntas;
        private Integer xpGanado;
        private LocalDateTime fecha;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CursoAlumnoDTO {
        private Long idCurso;
        private String nombreCurso;
        private String descripcion;
        private String icono;
        private String dificultad;
        private Integer leccionesCompletadas;
    }
}
