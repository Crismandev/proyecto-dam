package com.techmentor.service;

import com.techmentor.dto.DashboardDTO;
import com.techmentor.entity.ProgresoLeccion;
import com.techmentor.entity.Usuario;
import com.techmentor.repository.CursoRepository;
import com.techmentor.repository.EjercicioRepository;
import com.techmentor.repository.LeccionRepository;
import com.techmentor.repository.ProgresoLeccionRepository;
import com.techmentor.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.time.Duration;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final UsuarioRepository usuarioRepository;
    private final CursoRepository cursoRepository;
    private final LeccionRepository leccionRepository;
    private final EjercicioRepository ejercicioRepository;
    private final ProgresoLeccionRepository progresoLeccionRepository;

    @Transactional(readOnly = true)
    public DashboardDTO obtenerResumen() {
        long totalAlumnos = usuarioRepository.countByRolNombre("ESTUDIANTE");
        long totalCursos = cursoRepository.count();
        long totalLecciones = leccionRepository.count();
        long totalEjercicios = ejercicioRepository.count();

        List<DashboardDTO.ActividadRecienteDTO> actividades = new ArrayList<>();

        List<Usuario> ultimosUsuarios = usuarioRepository.findTop5ByOrderByFechaRegistroDesc();
        for (Usuario u : ultimosUsuarios) {
            LocalDateTime fechaReg = u.getFechaRegistro() != null ? u.getFechaRegistro().toLocalDateTime() : LocalDateTime.now();
            actividades.add(DashboardDTO.ActividadRecienteDTO.builder()
                    .tipo("NUEVO_ALUMNO")
                    .titulo("Nuevo alumno registrado")
                    .descripcion(u.getNombres() + (u.getApellidos() != null ? " " + u.getApellidos() : ""))
                    .fecha(fechaReg)
                    .tiempoRelativo(calcularTiempoRelativo(fechaReg))
                    .build());
        }

        List<ProgresoLeccion> ultimosProgresos = progresoLeccionRepository.findTop5ByOrderByFechaCompletadoDesc();
        for (ProgresoLeccion p : ultimosProgresos) {
            String cursoNombre = p.getLeccion() != null && p.getLeccion().getNivel() != null && p.getLeccion().getNivel().getCurso() != null
                    ? p.getLeccion().getNivel().getCurso().getNombre() : "";
            String userNombre = p.getUsuario() != null ? p.getUsuario().getNombres() : "";
            LocalDateTime fechaComp = p.getFechaCompletado() != null ? p.getFechaCompletado().toLocalDateTime() : LocalDateTime.now();

            actividades.add(DashboardDTO.ActividadRecienteDTO.builder()
                    .tipo("LECCION_COMPLETADA")
                    .titulo("Lección completada")
                    .descripcion(userNombre + " - " + cursoNombre)
                    .fecha(fechaComp)
                    .tiempoRelativo(calcularTiempoRelativo(fechaComp))
                    .build());
        }

        actividades.sort(Comparator.comparing(DashboardDTO.ActividadRecienteDTO::getFecha, Comparator.nullsLast(Comparator.naturalOrder())).reversed());
        if (actividades.size() > 5) {
            actividades = actividades.subList(0, 5);
        }

        List<Object[]> alumnosPorCursoData = progresoLeccionRepository.countAlumnosPorCurso();
        List<DashboardDTO.AlumnosPorCursoDTO> alumnosPorCurso = new ArrayList<>();
        for (Object[] row : alumnosPorCursoData) {
            alumnosPorCurso.add(DashboardDTO.AlumnosPorCursoDTO.builder()
                    .curso((String) row[0])
                    .alumnos(((Number) row[1]).longValue())
                    .build());
        }

        return DashboardDTO.builder()
                .totalAlumnos(totalAlumnos)
                .totalCursos(totalCursos)
                .totalLecciones(totalLecciones)
                .totalEjercicios(totalEjercicios)
                .actividadReciente(actividades)
                .alumnosPorCurso(alumnosPorCurso)
                .build();
    }

    private String calcularTiempoRelativo(LocalDateTime fecha) {
        if (fecha == null) return "";
        Duration duration = Duration.between(fecha, LocalDateTime.now());
        long horas = duration.toHours();
        if (horas < 1) {
            long minutos = duration.toMinutes();
            return "Hace " + (minutos <= 1 ? "1 minuto" : minutos + " minutos");
        } else if (horas < 24) {
            return "Hace " + (horas == 1 ? "1 hora" : horas + " horas");
        } else {
            long dias = duration.toDays();
            return "Hace " + (dias == 1 ? "1 día" : dias + " días");
        }
    }
}
