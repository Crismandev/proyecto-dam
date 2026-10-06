package com.techmentor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDTO {
    private Long totalAlumnos;
    private Long totalCursos;
    private Long totalLecciones;
    private Long totalEjercicios;
    private List<ActividadRecienteDTO> actividadReciente;
    private List<AlumnosPorCursoDTO> alumnosPorCurso;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ActividadRecienteDTO {
        private String tipo; // "NUEVO_ALUMNO", "LECCION_COMPLETADA"
        private String titulo; // "Nuevo alumno registrado", "Lección completada"
        private String descripcion; // "Ana Torres", "Juan Pérez - APIs con Spring Boot"
        private LocalDateTime fecha;
        private String tiempoRelativo; // "Hace 2 horas"
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AlumnosPorCursoDTO {
        private String curso;
        private Long alumnos;
    }
}
