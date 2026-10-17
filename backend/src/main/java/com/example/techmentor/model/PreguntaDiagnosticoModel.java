package com.example.techmentor.model;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.example.techmentor.bean.dto.PreguntaDiagnosticoDto;
import com.example.techmentor.bean.entity.CategoriaPregunta;
import com.example.techmentor.bean.entity.ExamenDiagnostico;
import com.example.techmentor.bean.entity.PreguntaDiagnostico;
import com.example.techmentor.bean.mapper.PreguntaDiagnosticoMapper;
import com.example.techmentor.persistence.CategoriaPreguntaRepository;
import com.example.techmentor.persistence.ExamenDiagnosticoRepository;
import com.example.techmentor.persistence.PreguntaDiagnosticoRepository;
import com.example.techmentor.usecase.PreguntaDiagnosticoUseCase;

/** Lógica de preguntas del diagnóstico (staff). */
@Service
@RequiredArgsConstructor
public class PreguntaDiagnosticoModel implements PreguntaDiagnosticoUseCase {

    private final PreguntaDiagnosticoRepository preguntaRepository;
    private final ExamenDiagnosticoRepository examenRepository;
    private final CategoriaPreguntaRepository categoriaPreguntaRepository;
    private final PreguntaDiagnosticoMapper preguntaMapper;

    @Override
    @Transactional(readOnly = true)
    public List<PreguntaDiagnosticoDto> listarPorExamen(Long idExamen) {
        return preguntaRepository.findByExamen_IdExamenOrderByOrdenAsc(idExamen)
                .stream()
                .map(preguntaMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PreguntaDiagnosticoDto obtener(Long id) {
        return preguntaMapper.toDto(buscar(id));
    }

    @Override
    @Transactional
    public PreguntaDiagnosticoDto crear(PreguntaDiagnosticoDto dto) {
        PreguntaDiagnostico entity = preguntaMapper.toEntity(dto);
        entity.setExamen(resolverExamen(dto.getIdExamen()));
        entity.setCategoriaPregunta(resolverCategoria(dto.getIdCategoriaPregunta()));
        return preguntaMapper.toDto(preguntaRepository.saveAndFlush(entity));
    }

    @Override
    @Transactional
    public PreguntaDiagnosticoDto actualizar(Long id, PreguntaDiagnosticoDto dto) {
        PreguntaDiagnostico entity = buscar(id);
        preguntaMapper.actualizar(dto, entity);
        entity.setExamen(resolverExamen(dto.getIdExamen()));
        entity.setCategoriaPregunta(resolverCategoria(dto.getIdCategoriaPregunta()));
        return preguntaMapper.toDto(preguntaRepository.saveAndFlush(entity));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        preguntaRepository.delete(buscar(id));
        preguntaRepository.flush();
    }

    private PreguntaDiagnostico buscar(Long id) {
        return preguntaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Pregunta no encontrada: " + id));
    }

    private ExamenDiagnostico resolverExamen(Long idExamen) {
        return examenRepository.findById(idExamen)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Examen no encontrado: " + idExamen));
    }

    private CategoriaPregunta resolverCategoria(Long idCategoria) {
        return categoriaPreguntaRepository.findById(idCategoria)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Categoría de pregunta no encontrada: " + idCategoria));
    }
}
