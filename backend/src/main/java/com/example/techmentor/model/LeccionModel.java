package com.example.techmentor.model;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.example.techmentor.bean.dto.LeccionDto;
import com.example.techmentor.bean.entity.Leccion;
import com.example.techmentor.bean.entity.Nivel;
import com.example.techmentor.bean.mapper.LeccionMapper;
import com.example.techmentor.persistence.LeccionRepository;
import com.example.techmentor.persistence.NivelRepository;
import com.example.techmentor.usecase.LeccionUseCase;

/** Lógica de negocio de lección: implementa LeccionUseCase usando repositorio y mapper. */
@Service
@RequiredArgsConstructor
public class LeccionModel implements LeccionUseCase {

    private final LeccionRepository leccionRepository;
    private final LeccionMapper leccionMapper;
    private final NivelRepository nivelRepository;

    @Override
    @Transactional(readOnly = true)
    public List<LeccionDto> listar() {
        return leccionRepository.findAll().stream().map(leccionMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LeccionDto> listarPorNivel(Long idNivel) {
        return leccionRepository.findByNivel_IdNivelOrderByOrdenAsc(idNivel).stream().map(leccionMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public LeccionDto obtener(Long id) {
        return leccionMapper.toDto(buscar(id));
    }

    @Override
    @Transactional
    public LeccionDto crear(LeccionDto dto) {
        Leccion entity = leccionMapper.toEntity(dto);
        entity.setNivel(resolverNivel(dto.getIdNivel()));
        return leccionMapper.toDto(leccionRepository.saveAndFlush(entity));
    }

    @Override
    @Transactional
    public LeccionDto actualizar(Long id, LeccionDto dto) {
        Leccion entity = buscar(id);
        leccionMapper.actualizar(dto, entity);
        entity.setNivel(resolverNivel(dto.getIdNivel()));
        return leccionMapper.toDto(leccionRepository.saveAndFlush(entity));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        leccionRepository.delete(buscar(id));
        leccionRepository.flush();
    }

    private Leccion buscar(Long id) {
        return leccionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No existe lección con id: " + id));
    }

    private Nivel resolverNivel(Long id) {
        return nivelRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No existe nivel con id: " + id));
    }

}
