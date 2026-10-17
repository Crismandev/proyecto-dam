package com.example.techmentor.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.example.techmentor.bean.dto.RendirExamenDto;
import com.example.techmentor.bean.dto.RespuestaEnvioDto;
import com.example.techmentor.bean.dto.ResultadoDiagnosticoDto;
import com.example.techmentor.bean.entity.Curso;
import com.example.techmentor.bean.entity.ExamenDiagnostico;
import com.example.techmentor.bean.entity.Nivel;
import com.example.techmentor.bean.entity.OpcionDiagnostico;
import com.example.techmentor.bean.entity.PreguntaDiagnostico;
import com.example.techmentor.bean.entity.RespuestaDiagnostico;
import com.example.techmentor.bean.entity.ResultadoDiagnostico;
import com.example.techmentor.bean.mapper.ResultadoDiagnosticoMapper;
import com.example.techmentor.persistence.ExamenDiagnosticoRepository;
import com.example.techmentor.persistence.NivelRepository;
import com.example.techmentor.persistence.OpcionDiagnosticoRepository;
import com.example.techmentor.persistence.PreguntaDiagnosticoRepository;
import com.example.techmentor.persistence.RespuestaDiagnosticoRepository;
import com.example.techmentor.persistence.ResultadoDiagnosticoRepository;
import com.example.techmentor.persistence.UsuarioRepository;
import com.example.techmentor.security.UsuarioAutenticado;
import com.example.techmentor.usecase.ResultadoDiagnosticoUseCase;

/** Rinde exámenes: valida respuestas, calcula puntaje, asigna nivel y guarda resultado y respuestas. */
@Service
@RequiredArgsConstructor
public class ResultadoDiagnosticoModel implements ResultadoDiagnosticoUseCase {

    private final ResultadoDiagnosticoRepository resultadoRepository;
    private final RespuestaDiagnosticoRepository respuestaRepository;
    private final ExamenDiagnosticoRepository examenRepository;
    private final PreguntaDiagnosticoRepository preguntaRepository;
    private final OpcionDiagnosticoRepository opcionRepository;
    private final NivelRepository nivelRepository;
    private final UsuarioRepository usuarioRepository;
    private final NivelAsignador nivelAsignador;
    private final ResultadoDiagnosticoMapper resultadoMapper;

    /**
     * Rinde un examen: valida las respuestas, calcula el puntaje,
     * asigna el nivel (si el examen tiene curso) y guarda resultado + respuestas.
     */
    @Override
    @Transactional
    public ResultadoDiagnosticoDto rendir(Long idExamen, RendirExamenDto dto, UsuarioAutenticado usuario) {
        ExamenDiagnostico examen = examenRepository.findById(idExamen)
                .filter(e -> Boolean.TRUE.equals(e.getActivo()))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Examen no encontrado o inactivo: " + idExamen));

        List<PreguntaDiagnostico> preguntas = preguntaRepository.findByExamen_IdExamenOrderByOrdenAsc(idExamen);
        if (preguntas.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El examen no tiene preguntas");
        }

        Map<Long, PreguntaDiagnostico> preguntasPorId = preguntas.stream()
                .collect(Collectors.toMap(PreguntaDiagnostico::getIdPregunta, Function.identity()));
        Map<Long, OpcionDiagnostico> opcionesPorId = opcionRepository.findAllByExamen(idExamen).stream()
                .collect(Collectors.toMap(OpcionDiagnostico::getIdOpcionDiagnostico, Function.identity()));

        // idPregunta -> idOpcionSeleccionada (puede ser null = sin responder)
        Map<Long, Long> seleccion = new HashMap<>();
        for (RespuestaEnvioDto r : dto.getRespuestas()) {
            if (!preguntasPorId.containsKey(r.getIdPregunta())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "La pregunta " + r.getIdPregunta() + " no pertenece al examen");
            }
            if (seleccion.containsKey(r.getIdPregunta())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "La pregunta " + r.getIdPregunta() + " viene repetida");
            }
            if (r.getIdOpcionSeleccionada() != null) {
                OpcionDiagnostico opcion = opcionesPorId.get(r.getIdOpcionSeleccionada());
                if (opcion == null || !opcion.getPregunta().getIdPregunta().equals(r.getIdPregunta())) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "La opción " + r.getIdOpcionSeleccionada()
                                    + " no pertenece a la pregunta " + r.getIdPregunta());
                }
            }
            seleccion.put(r.getIdPregunta(), r.getIdOpcionSeleccionada());
        }

        // Puntaje
        int puntajeMaximo = 0;
        int puntajeObtenido = 0;
        for (PreguntaDiagnostico p : preguntas) {
            puntajeMaximo += p.getPuntaje();
            Long idOpcion = seleccion.get(p.getIdPregunta());
            if (idOpcion != null && Boolean.TRUE.equals(opcionesPorId.get(idOpcion).getEsCorrecta())) {
                puntajeObtenido += p.getPuntaje();
            }
        }

        // Nivel (solo si el examen está ligado a un curso)
        Nivel nivel = null;
        if (examen.getCurso() != null) {
            double porcentaje = puntajeMaximo == 0 ? 0 : puntajeObtenido * 100.0 / puntajeMaximo;
            nivel = nivelAsignador.asignar(examen.getCurso().getIdCurso(), porcentaje).orElse(null);
        }

        ResultadoDiagnostico resultado = resultadoRepository.save(ResultadoDiagnostico.builder()
                .usuario(usuarioRepository.getReferenceById(usuario.idUsuario()))
                .examen(examen)
                .nivelAsignado(nivel)
                .puntajeObtenido(puntajeObtenido)
                .puntajeMaximo(puntajeMaximo)
                .build());

        // Una fila por pregunta (null en opción = sin responder)
        List<RespuestaDiagnostico> respuestas = preguntas.stream()
                .map(p -> {
                    Long idOpcion = seleccion.get(p.getIdPregunta());
                    return RespuestaDiagnostico.builder()
                            .resultado(resultado)
                            .pregunta(p)
                            .opcionSeleccionada(idOpcion == null ? null : opcionesPorId.get(idOpcion))
                            .build();
                })
                .toList();
        respuestaRepository.saveAll(respuestas);

        return resultadoMapper.toDto(resultado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResultadoDiagnosticoDto> misResultados(UsuarioAutenticado usuario) {
        return resultadoRepository.findByUsuario_IdUsuarioOrderByFechaRealizacionDesc(usuario.idUsuario())
                .stream()
                .map(resultadoMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ResultadoDiagnosticoDto obtener(Long id, UsuarioAutenticado usuario) {
        ResultadoDiagnostico resultado = buscar(id);
        usuario.exigirPropietarioOStaff(resultado.getUsuario().getIdUsuario());
        return resultadoMapper.toDto(resultado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResultadoDiagnosticoDto> listarPorUsuario(Long idUsuario) {
        return resultadoRepository.findByUsuario_IdUsuarioOrderByFechaRealizacionDesc(idUsuario)
                .stream()
                .map(resultadoMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResultadoDiagnosticoDto> listarPorExamen(Long idExamen) {
        return resultadoRepository.findByExamen_IdExamenOrderByFechaRealizacionDesc(idExamen)
                .stream()
                .map(resultadoMapper::toDto)
                .toList();
    }

    /** Cambio manual del nivel asignado (el nivel debe ser del curso del examen). */
    @Override
    @Transactional
    public ResultadoDiagnosticoDto cambiarNivel(Long id, Long idNivel) {
        ResultadoDiagnostico resultado = buscar(id);
        Nivel nivel = nivelRepository.findById(idNivel)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Nivel no encontrado: " + idNivel));

        Curso cursoExamen = resultado.getExamen().getCurso();
        if (cursoExamen == null || !cursoExamen.getIdCurso().equals(nivel.getCurso().getIdCurso())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El nivel no pertenece al curso del examen");
        }

        resultado.setNivelAsignado(nivel);
        return resultadoMapper.toDto(resultadoRepository.saveAndFlush(resultado));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        resultadoRepository.delete(buscar(id));
        resultadoRepository.flush();
    }

    private ResultadoDiagnostico buscar(Long id) {
        return resultadoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Resultado no encontrado: " + id));
    }
}
