package com.example.techmentor.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.example.techmentor.bean.dto.AnalisisCategoriaDto;
import com.example.techmentor.bean.dto.RecomendacionCursoDto;
import com.example.techmentor.bean.entity.*;
import com.example.techmentor.bean.mapper.RecomendacionCursoMapper;
import com.example.techmentor.persistence.*;
import com.example.techmentor.security.UsuarioAutenticado;
import com.example.techmentor.usecase.ExplicadorIaUseCase;
import com.example.techmentor.usecase.RecomendacionUseCase;

/**
 * Recomienda cursos con REGLAS guardadas en la BD:
 *  1) categoría de pregunta con % menor al umbral  -> curso que la refuerza (categoria_pregunta_cursos)
 *  2) puesto (profesión) del examen o del usuario  -> cursos que exige (profesion_cursos)
 * Se descartan cursos inactivos o ya completados. La IA solo agrega una explicación breve (opcional).
 */
@Service
@RequiredArgsConstructor
public class RecomendacionModel implements RecomendacionUseCase {

    private static final Set<String> ESTADOS = Set.of("PENDIENTE", "INICIADA", "DESCARTADA");

    private final RecomendacionCursoRepository recomendacionRepository;
    private final ResultadoDiagnosticoRepository resultadoRepository;
    private final RespuestaDiagnosticoRepository respuestaRepository;
    private final CategoriaPreguntaCursoRepository categoriaPreguntaCursoRepository;
    private final ProfesionCursoRepository profesionCursoRepository;
    private final UsuarioProfesionRepository usuarioProfesionRepository;
    private final ProgresoCursoRepository progresoCursoRepository;
    private final RecomendacionCursoMapper recomendacionMapper;
    private final ExplicadorIaUseCase explicadorIa;

    @Override
    @Transactional
    public List<RecomendacionCursoDto> generar(Long idResultado, UsuarioAutenticado solicitante) {
        ResultadoDiagnostico resultado = buscarResultado(idResultado);
        Long idUsuario = resultado.getUsuario().getIdUsuario();
        solicitante.exigirPropietarioOStaff(idUsuario);

        List<AnalisisCategoriaDto> analisis = analizar(idResultado);
        Map<Long, Candidato> candidatos = new LinkedHashMap<>();

        // Regla 1: categorías de pregunta con bajo rendimiento
        for (AnalisisCategoriaDto a : analisis) {
            for (CategoriaPreguntaCurso rel : categoriaPreguntaCursoRepository
                    .findByCategoriaPregunta_IdCategoriaPregunta(a.getIdCategoriaPregunta())) {
                if (a.getPorcentaje() < rel.getUmbralPorcentaje()) {
                    int prioridad = a.getPorcentaje() < 40 ? 1 : 2;
                    agregar(candidatos, rel.getCurso(), prioridad, "Rendimiento bajo en «" + a.getCategoria() + "»: "
                            + a.getPorcentaje() + "% (mínimo esperado " + rel.getUmbralPorcentaje() + "%)");
                }
            }
        }

        // Regla 2: cursos que exige el puesto (el del examen o, si es general, el principal del usuario)
        Profesion profesion = resultado.getExamen().getProfesion();
        if (profesion == null) {
            profesion = usuarioProfesionRepository.findByUsuario_IdUsuarioAndPrincipalTrue(idUsuario)
                    .map(UsuarioProfesion::getProfesion).orElse(null);
        }
        if (profesion != null) {
            for (ProfesionCurso pc : profesionCursoRepository.findByProfesion_IdProfesion(profesion.getIdProfesion())) {
                agregar(candidatos, pc.getCurso(), pc.getPrioridad(),
                        "Requerido para el puesto «" + profesion.getNombre() + "»");
            }
        }

        Set<Long> completados = progresoCursoRepository.listarPorUsuario(idUsuario).stream()
                .filter(p -> p.getEstado() == EstadoProgreso.COMPLETADO)
                .map(p -> p.getCurso().getIdCurso())
                .collect(Collectors.toSet());

        // Se reemplazan las recomendaciones anteriores de este resultado
        recomendacionRepository.deleteByResultado_IdResultado(idResultado);
        recomendacionRepository.flush();

        List<RecomendacionCurso> nuevas = new ArrayList<>();
        for (Candidato c : candidatos.values()) {
            if (!Boolean.TRUE.equals(c.curso.getActivo()) || completados.contains(c.curso.getIdCurso())) {
                continue;
            }
            nuevas.add(RecomendacionCurso.builder()
                    .usuario(resultado.getUsuario())
                    .resultado(resultado)
                    .curso(c.curso)
                    .profesion(profesion)
                    .prioridad(c.prioridad)
                    .motivo(String.join("; ", c.motivos))
                    .build());
        }
        nuevas.sort(Comparator.comparing(RecomendacionCurso::getPrioridad)
                .thenComparing(r -> r.getCurso().getNombre()));
        List<RecomendacionCurso> guardadas = recomendacionRepository.saveAllAndFlush(nuevas);

        // Puerta abierta a la IA: si hay explicaciones se guardan junto a cada recomendación
        Map<Long, String> explicaciones = explicadorIa.explicar(idUsuario, analisis, guardadas);
        if (!explicaciones.isEmpty()) {
            for (RecomendacionCurso r : guardadas) {
                String texto = explicaciones.get(r.getCurso().getIdCurso());
                if (texto != null) {
                    r.setExplicacionIa(texto);
                }
            }
            recomendacionRepository.saveAllAndFlush(guardadas);
        }
        return guardadas.stream().map(recomendacionMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AnalisisCategoriaDto> analisis(Long idResultado, UsuarioAutenticado solicitante) {
        ResultadoDiagnostico resultado = buscarResultado(idResultado);
        solicitante.exigirPropietarioOStaff(resultado.getUsuario().getIdUsuario());
        return analizar(idResultado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecomendacionCursoDto> misRecomendaciones(Long idUsuario) {
        return listarPorUsuario(idUsuario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecomendacionCursoDto> porResultado(Long idResultado, UsuarioAutenticado solicitante) {
        ResultadoDiagnostico resultado = buscarResultado(idResultado);
        solicitante.exigirPropietarioOStaff(resultado.getUsuario().getIdUsuario());
        return recomendacionRepository.findByResultado_IdResultadoOrderByPrioridadAsc(idResultado)
                .stream().map(recomendacionMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecomendacionCursoDto> listarPorUsuario(Long idUsuario) {
        return recomendacionRepository.findByUsuario_IdUsuarioOrderByPrioridadAscFechaCreacionDesc(idUsuario)
                .stream().map(recomendacionMapper::toDto).toList();
    }

    @Override
    @Transactional
    public RecomendacionCursoDto cambiarEstado(Long id, String estado, UsuarioAutenticado solicitante) {
        if (!ESTADOS.contains(estado)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Estado inválido. Usa PENDIENTE, INICIADA o DESCARTADA");
        }
        RecomendacionCurso r = recomendacionRepository.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Recomendación no encontrada: " + id));
        solicitante.exigirPropietarioOStaff(r.getUsuario().getIdUsuario());
        r.setEstado(estado);
        return recomendacionMapper.toDto(recomendacionRepository.saveAndFlush(r));
    }

    // ---------------------------------------------------------------- auxiliares

    private ResultadoDiagnostico buscarResultado(Long id) {
        return resultadoRepository.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Resultado no encontrado: " + id));
    }

    /** Suma puntajes por categoría de pregunta a partir de las respuestas guardadas. */
    private List<AnalisisCategoriaDto> analizar(Long idResultado) {
        Map<Long, AnalisisCategoriaDto> porCategoria = new LinkedHashMap<>();
        for (RespuestaDiagnostico r : respuestaRepository.findByResultado_IdResultado(idResultado)) {
            PreguntaDiagnostico pregunta = r.getPregunta();
            CategoriaPregunta categoria = pregunta.getCategoriaPregunta();
            AnalisisCategoriaDto a = porCategoria.computeIfAbsent(categoria.getIdCategoriaPregunta(),
                    k -> AnalisisCategoriaDto.builder()
                            .idCategoriaPregunta(k).categoria(categoria.getNombre())
                            .puntajeObtenido(0).puntajeMaximo(0).porcentaje(0.0).build());
            a.setPuntajeMaximo(a.getPuntajeMaximo() + pregunta.getPuntaje());
            if (r.getOpcionSeleccionada() != null && Boolean.TRUE.equals(r.getOpcionSeleccionada().getEsCorrecta())) {
                a.setPuntajeObtenido(a.getPuntajeObtenido() + pregunta.getPuntaje());
            }
        }
        porCategoria.values().forEach(a -> a.setPorcentaje(a.getPuntajeMaximo() == 0 ? 0.0
                : Math.round(a.getPuntajeObtenido() * 10000.0 / a.getPuntajeMaximo()) / 100.0));
        return new ArrayList<>(porCategoria.values());
    }

    private void agregar(Map<Long, Candidato> mapa, Curso curso, int prioridad, String motivo) {
        Candidato c = mapa.computeIfAbsent(curso.getIdCurso(), k -> {
            Candidato nuevo = new Candidato();
            nuevo.curso = curso;
            nuevo.prioridad = prioridad;
            return nuevo;
        });
        c.prioridad = Math.min(c.prioridad, prioridad);
        c.motivos.add(motivo);
    }

    /** Curso candidato con su prioridad (la más alta gana) y todos los motivos acumulados. */
    private static final class Candidato {
        private Curso curso;
        private int prioridad;
        private final List<String> motivos = new ArrayList<>();
    }
}
