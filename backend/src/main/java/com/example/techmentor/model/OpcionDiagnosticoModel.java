package com.example.techmentor.model;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.example.techmentor.bean.dto.OpcionDiagnosticoDto;
import com.example.techmentor.bean.entity.OpcionDiagnostico;
import com.example.techmentor.bean.entity.PreguntaDiagnostico;
import com.example.techmentor.bean.mapper.OpcionDiagnosticoMapper;
import com.example.techmentor.persistence.OpcionDiagnosticoRepository;
import com.example.techmentor.persistence.PreguntaDiagnosticoRepository;
import com.example.techmentor.usecase.OpcionDiagnosticoUseCase;

/** Lógica de opciones de las preguntas del diagnóstico (staff). */
@Service
@RequiredArgsConstructor
public class OpcionDiagnosticoModel implements OpcionDiagnosticoUseCase {

    private final OpcionDiagnosticoRepository opcionRepository;
    private final PreguntaDiagnosticoRepository preguntaRepository;
    private final OpcionDiagnosticoMapper opcionMapper;

    @Override
    @Transactional(readOnly = true)
    public List<OpcionDiagnosticoDto> listarPorPregunta(Long idPregunta) {
        return opcionRepository.findByPregunta_IdPreguntaOrderByOrdenAsc(idPregunta)
                .stream()
                .map(opcionMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public OpcionDiagnosticoDto obtener(Long id) {
        return opcionMapper.toDto(buscar(id));
    }

    @Override
    @Transactional
    public OpcionDiagnosticoDto crear(OpcionDiagnosticoDto dto) {
        OpcionDiagnostico entity = opcionMapper.toEntity(dto);
        entity.setPregunta(resolverPregunta(dto.getIdPregunta()));
        return opcionMapper.toDto(opcionRepository.saveAndFlush(entity));
    }

    @Override
    @Transactional
    public OpcionDiagnosticoDto actualizar(Long id, OpcionDiagnosticoDto dto) {
        OpcionDiagnostico entity = buscar(id);
        opcionMapper.actualizar(dto, entity);
        entity.setPregunta(resolverPregunta(dto.getIdPregunta()));
        return opcionMapper.toDto(opcionRepository.saveAndFlush(entity));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        opcionRepository.delete(buscar(id));
        opcionRepository.flush();
    }

    private OpcionDiagnostico buscar(Long id) {
        return opcionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Opción no encontrada: " + id));
    }

    private PreguntaDiagnostico resolverPregunta(Long idPregunta) {
        return preguntaRepository.findById(idPregunta)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Pregunta no encontrada: " + idPregunta));
    }
}
