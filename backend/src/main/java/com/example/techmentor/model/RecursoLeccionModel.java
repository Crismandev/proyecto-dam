package com.example.techmentor.model;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.example.techmentor.bean.dto.RecursoLeccionDto;
import com.example.techmentor.bean.entity.RecursoLeccion;
import com.example.techmentor.bean.entity.Leccion;
import com.example.techmentor.bean.mapper.RecursoLeccionMapper;
import com.example.techmentor.persistence.RecursoLeccionRepository;
import com.example.techmentor.persistence.LeccionRepository;
import com.example.techmentor.usecase.RecursoLeccionUseCase;

/** Lógica de negocio de recurso de lección: implementa RecursoLeccionUseCase usando repositorio y mapper. */
@Service
@RequiredArgsConstructor
public class RecursoLeccionModel implements RecursoLeccionUseCase {

    private final RecursoLeccionRepository recursoLeccionRepository;
    private final RecursoLeccionMapper recursoLeccionMapper;
    private final LeccionRepository leccionRepository;

    @Override
    @Transactional(readOnly = true)
    public List<RecursoLeccionDto> listar() {
        return recursoLeccionRepository.findAll().stream().map(recursoLeccionMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecursoLeccionDto> listarPorLeccion(Long idLeccion) {
        return recursoLeccionRepository.findByLeccion_IdLeccionOrderByOrdenAsc(idLeccion).stream().map(recursoLeccionMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public RecursoLeccionDto obtener(Long id) {
        return recursoLeccionMapper.toDto(buscar(id));
    }

    @Override
    @Transactional
    public RecursoLeccionDto crear(RecursoLeccionDto dto) {
        RecursoLeccion entity = recursoLeccionMapper.toEntity(dto);
        entity.setLeccion(resolverLeccion(dto.getIdLeccion()));
        return recursoLeccionMapper.toDto(recursoLeccionRepository.saveAndFlush(entity));
    }

    @Override
    @Transactional
    public RecursoLeccionDto actualizar(Long id, RecursoLeccionDto dto) {
        RecursoLeccion entity = buscar(id);
        recursoLeccionMapper.actualizar(dto, entity);
        entity.setLeccion(resolverLeccion(dto.getIdLeccion()));
        return recursoLeccionMapper.toDto(recursoLeccionRepository.saveAndFlush(entity));
    }

    @Override
    @Transactional
    public RecursoLeccionDto cambiarActivo(Long id, boolean valor) {
        RecursoLeccion entity = buscar(id);
        entity.setActivo(valor);
        return recursoLeccionMapper.toDto(recursoLeccionRepository.saveAndFlush(entity));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        recursoLeccionRepository.delete(buscar(id));
        recursoLeccionRepository.flush();
    }

    private RecursoLeccion buscar(Long id) {
        return recursoLeccionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No existe recurso de lección con id: " + id));
    }

    private Leccion resolverLeccion(Long id) {
        return leccionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No existe leccion con id: " + id));
    }

}
