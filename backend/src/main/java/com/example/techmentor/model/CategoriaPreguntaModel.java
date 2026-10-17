package com.example.techmentor.model;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.example.techmentor.bean.dto.CategoriaPreguntaDto;
import com.example.techmentor.bean.entity.CategoriaPregunta;
import com.example.techmentor.bean.mapper.CategoriaPreguntaMapper;
import com.example.techmentor.persistence.CategoriaPreguntaRepository;
import com.example.techmentor.usecase.CategoriaPreguntaUseCase;

/** Lógica de categorías de pregunta del diagnóstico. */
@Service
@RequiredArgsConstructor
public class CategoriaPreguntaModel implements CategoriaPreguntaUseCase {

    private final CategoriaPreguntaRepository categoriaPreguntaRepository;
    private final CategoriaPreguntaMapper categoriaPreguntaMapper;

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaPreguntaDto> listar() {
        return categoriaPreguntaRepository.findAll()
                .stream()
                .map(categoriaPreguntaMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CategoriaPreguntaDto obtener(Long id) {
        return categoriaPreguntaMapper.toDto(buscar(id));
    }

    @Override
    @Transactional
    public CategoriaPreguntaDto crear(CategoriaPreguntaDto dto) {
        CategoriaPregunta entity = categoriaPreguntaMapper.toEntity(dto);
        return categoriaPreguntaMapper.toDto(categoriaPreguntaRepository.saveAndFlush(entity));
    }

    @Override
    @Transactional
    public CategoriaPreguntaDto actualizar(Long id, CategoriaPreguntaDto dto) {
        CategoriaPregunta entity = buscar(id);
        categoriaPreguntaMapper.actualizar(dto, entity);
        return categoriaPreguntaMapper.toDto(categoriaPreguntaRepository.saveAndFlush(entity));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        categoriaPreguntaRepository.delete(buscar(id));
        categoriaPreguntaRepository.flush();
    }

    private CategoriaPregunta buscar(Long id) {
        return categoriaPreguntaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Categoría de pregunta no encontrada: " + id));
    }
}
