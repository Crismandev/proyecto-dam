package com.example.techmentor.model;

import java.time.OffsetDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import com.example.techmentor.bean.entity.Curso;
import com.example.techmentor.bean.entity.EstadoProgreso;
import com.example.techmentor.bean.entity.Leccion;
import com.example.techmentor.bean.entity.Nivel;
import com.example.techmentor.bean.entity.ProgresoCurso;
import com.example.techmentor.bean.entity.ResultadoDiagnostico;
import com.example.techmentor.persistence.CursoRepository;
import com.example.techmentor.persistence.LeccionRepository;
import com.example.techmentor.persistence.NivelRepository;
import com.example.techmentor.persistence.ProgresoCursoRepository;
import com.example.techmentor.persistence.ProgresoLeccionRepository;
import com.example.techmentor.persistence.ResultadoDiagnosticoRepository;
import com.example.techmentor.persistence.UsuarioRepository;

/**
 * Reglas de progreso del curso. Se usa dentro de métodos @Transactional de los Model.
 */
@Component
@RequiredArgsConstructor
public class ProgresoCursoGestor {

    private final ProgresoCursoRepository progresoCursoRepository;
    private final ProgresoLeccionRepository progresoLeccionRepository;
    private final CursoRepository cursoRepository;
    private final LeccionRepository leccionRepository;
    private final NivelRepository nivelRepository;
    private final ResultadoDiagnosticoRepository resultadoRepository;
    private final UsuarioRepository usuarioRepository;

    /**
     * Crea el progreso del curso o reanuda el existente (NO_INICIADO / ABANDONADO).
     * Al crearlo, el nivel inicial es el último nivel asignado por un diagnóstico del mismo curso;
     * si no hay diagnóstico, el primer nivel del curso.
     */
    public ProgresoCurso iniciarOReanudar(Long idUsuario, Long idCurso) {
        OffsetDateTime ahora = OffsetDateTime.now();

        Optional<ProgresoCurso> existente =
                progresoCursoRepository.findByUsuario_IdUsuarioAndCurso_IdCurso(idUsuario, idCurso);
        if (existente.isPresent()) {
            ProgresoCurso progreso = existente.get();
            if (progreso.getEstado() == EstadoProgreso.ABANDONADO
                    || progreso.getEstado() == EstadoProgreso.NO_INICIADO) {
                progreso.setEstado(EstadoProgreso.EN_CURSO);
                if (progreso.getFechaInicio() == null) {
                    progreso.setFechaInicio(ahora);
                }
            }
            progreso.setFechaUltimaActividad(ahora);
            return progreso;
        }

        Curso curso = cursoRepository.findById(idCurso)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Curso no encontrado: " + idCurso));
        if (!Boolean.TRUE.equals(curso.getActivo())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El curso no está activo");
        }

        Nivel nivelInicial = resultadoRepository
                .findFirstByUsuario_IdUsuarioAndExamen_Curso_IdCursoAndNivelAsignadoIsNotNullOrderByFechaRealizacionDesc(
                        idUsuario, idCurso)
                .map(ResultadoDiagnostico::getNivelAsignado)
                .orElseGet(() -> nivelRepository.findFirstByCurso_IdCursoOrderByOrdenAsc(idCurso).orElse(null));

        return progresoCursoRepository.save(ProgresoCurso.builder()
                .usuario(usuarioRepository.getReferenceById(idUsuario))
                .curso(curso)
                .nivelActual(nivelInicial)
                .estado(EstadoProgreso.EN_CURSO)
                .fechaInicio(ahora)
                .fechaUltimaActividad(ahora)
                .build());
    }

    /**
     * Se llama la PRIMERA vez que el usuario completa una lección:
     * suma XP al progreso del curso, avanza de nivel si terminó el actual
     * y marca el curso COMPLETADO si ya no quedan lecciones.
     */
    public void registrarLeccionCompletada(Long idUsuario, Leccion leccion, int xpGanado) {
        Nivel nivelLeccion = leccion.getNivel();
        Long idCurso = nivelLeccion.getCurso().getIdCurso();
        OffsetDateTime ahora = OffsetDateTime.now();

        ProgresoCurso progreso = iniciarOReanudar(idUsuario, idCurso);
        progreso.setFechaUltimaActividad(ahora);
        progreso.setXpGanado(progreso.getXpGanado() + xpGanado);

        long totalCurso = leccionRepository.countByNivel_Curso_IdCurso(idCurso);
        long completadasCurso = progresoLeccionRepository
                .contarPorCurso(idUsuario, idCurso, EstadoProgreso.COMPLETADO);

        if (totalCurso > 0 && completadasCurso >= totalCurso) {
            progreso.setEstado(EstadoProgreso.COMPLETADO);
            progreso.setFechaCompletado(ahora);
        } else if (progreso.getNivelActual() != null
                && progreso.getNivelActual().getIdNivel().equals(nivelLeccion.getIdNivel())) {
            long totalNivel = leccionRepository.countByNivel_IdNivel(nivelLeccion.getIdNivel());
            long completadasNivel = progresoLeccionRepository
                    .contarPorNivel(idUsuario, nivelLeccion.getIdNivel(), EstadoProgreso.COMPLETADO);
            if (completadasNivel >= totalNivel) {
                nivelRepository
                        .findFirstByCurso_IdCursoAndOrdenGreaterThanOrderByOrdenAsc(idCurso, nivelLeccion.getOrden())
                        .ifPresent(progreso::setNivelActual);
            }
        }

        progresoCursoRepository.save(progreso);
    }
}
