package com.example.techmentor.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.example.techmentor.bean.dto.ExamenDiagnosticoDto;
import com.example.techmentor.bean.dto.ExamenParaRendirDto;
import com.example.techmentor.bean.dto.OpcionPublicaDto;
import com.example.techmentor.bean.dto.PreguntaPublicaDto;
import com.example.techmentor.bean.entity.Curso;
import com.example.techmentor.bean.entity.Profesion;
import com.example.techmentor.bean.entity.UsuarioProfesion;
import com.example.techmentor.bean.entity.ExamenDiagnostico;
import com.example.techmentor.bean.entity.OpcionDiagnostico;
import com.example.techmentor.bean.entity.PreguntaDiagnostico;
import com.example.techmentor.bean.mapper.ExamenDiagnosticoMapper;
import com.example.techmentor.bean.mapper.OpcionDiagnosticoMapper;
import com.example.techmentor.bean.mapper.PreguntaDiagnosticoMapper;
import com.example.techmentor.persistence.CursoRepository;
import com.example.techmentor.persistence.ExamenDiagnosticoRepository;
import com.example.techmentor.persistence.OpcionDiagnosticoRepository;
import com.example.techmentor.persistence.PreguntaDiagnosticoRepository;
import com.example.techmentor.persistence.ProfesionRepository;
import com.example.techmentor.persistence.UsuarioProfesionRepository;
import com.example.techmentor.usecase.ExamenDiagnosticoUseCase;

/** Lógica de exámenes diagnóstico: CRUD, filtro por curso/puesto y examen listo para rendir (sin respuestas correctas). */
@Service
@RequiredArgsConstructor
public class ExamenDiagnosticoModel implements ExamenDiagnosticoUseCase {

    private final ExamenDiagnosticoRepository examenRepository;
    private final PreguntaDiagnosticoRepository preguntaRepository;
    private final OpcionDiagnosticoRepository opcionRepository;
    private final CursoRepository cursoRepository;
    private final ProfesionRepository profesionRepository;
    private final UsuarioProfesionRepository usuarioProfesionRepository;
    private final ExamenDiagnosticoMapper examenMapper;
    private final PreguntaDiagnosticoMapper preguntaMapper;
    private final OpcionDiagnosticoMapper opcionMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ExamenDiagnosticoDto> listarActivos(Long idCurso, Long idProfesion) {
        List<ExamenDiagnostico> examenes;
        if (idCurso != null && idProfesion != null) {
            examenes = examenRepository.findByActivoTrueAndCurso_IdCursoAndProfesion_IdProfesion(idCurso, idProfesion);
        } else if (idCurso != null) {
            examenes = examenRepository.findByActivoTrueAndCurso_IdCurso(idCurso);
        } else if (idProfesion != null) {
            examenes = examenRepository.findByActivoTrueAndProfesion_IdProfesion(idProfesion);
        } else {
            examenes = examenRepository.findByActivoTrue();
        }
        return examenes.stream().map(examenMapper::toDto).toList();
    }

    /** Exámenes del puesto principal del usuario; si no tiene puesto, solo los generales. */
    @Override
    @Transactional(readOnly = true)
    public List<ExamenDiagnosticoDto> listarParaUsuario(Long idUsuario) {
        List<ExamenDiagnostico> examenes = usuarioProfesionRepository
                .findByUsuario_IdUsuarioAndPrincipalTrue(idUsuario)
                .map(UsuarioProfesion::getProfesion)
                .map(p -> examenRepository.findActivosParaProfesion(p.getIdProfesion()))
                .orElseGet(examenRepository::findByActivoTrueAndProfesionIsNull);
        return examenes.stream().map(examenMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExamenDiagnosticoDto> listarTodos() {
        return examenRepository.findAll().stream().map(examenMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ExamenDiagnosticoDto obtener(Long id) {
        return examenMapper.toDto(buscar(id));
    }

    /** Examen con preguntas y opciones SIN el campo esCorrecta. */
    @Override
    @Transactional(readOnly = true)
    public ExamenParaRendirDto obtenerParaRendir(Long id) {
        ExamenDiagnostico examen = buscar(id);
        if (!Boolean.TRUE.equals(examen.getActivo())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El examen no está activo");
        }

        Map<Long, List<OpcionPublicaDto>> opcionesPorPregunta = opcionRepository.findAllByExamen(id)
                .stream()
                .collect(Collectors.groupingBy(
                        (OpcionDiagnostico o) -> o.getPregunta().getIdPregunta(),
                        LinkedHashMap::new,
                        Collectors.mapping(opcionMapper::toPublicDto, Collectors.toList())));

        List<PreguntaPublicaDto> preguntas = preguntaRepository.findByExamen_IdExamenOrderByOrdenAsc(id)
                .stream()
                .map((PreguntaDiagnostico p) -> {
                    PreguntaPublicaDto dto = preguntaMapper.toPublicDto(p);
                    dto.setOpciones(opcionesPorPregunta.getOrDefault(p.getIdPregunta(), List.of()));
                    return dto;
                })
                .toList();

        return ExamenParaRendirDto.builder()
                .examen(examenMapper.toDto(examen))
                .preguntas(preguntas)
                .build();
    }

    @Override
    @Transactional
    public ExamenDiagnosticoDto crear(ExamenDiagnosticoDto dto) {
        ExamenDiagnostico entity = examenMapper.toEntity(dto);
        entity.setCurso(resolverCurso(dto.getIdCurso()));
        entity.setProfesion(resolverProfesion(dto.getIdProfesion()));
        return examenMapper.toDto(examenRepository.saveAndFlush(entity));
    }

    @Override
    @Transactional
    public ExamenDiagnosticoDto actualizar(Long id, ExamenDiagnosticoDto dto) {
        ExamenDiagnostico entity = buscar(id);
        examenMapper.actualizar(dto, entity);
        entity.setCurso(resolverCurso(dto.getIdCurso()));
        entity.setProfesion(resolverProfesion(dto.getIdProfesion()));
        return examenMapper.toDto(examenRepository.saveAndFlush(entity));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        examenRepository.delete(buscar(id));
        examenRepository.flush();
    }

    private ExamenDiagnostico buscar(Long id) {
        return examenRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Examen no encontrado: " + id));
    }

    private Profesion resolverProfesion(Long idProfesion) {
        if (idProfesion == null) {
            return null;
        }
        return profesionRepository.findById(idProfesion)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Profesión no encontrada: " + idProfesion));
    }

    private Curso resolverCurso(Long idCurso) {
        if (idCurso == null) {
            return null;
        }
        return cursoRepository.findById(idCurso)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Curso no encontrado: " + idCurso));
    }
}
