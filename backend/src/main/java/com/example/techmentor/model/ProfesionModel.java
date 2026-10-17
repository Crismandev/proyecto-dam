package com.example.techmentor.model;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.example.techmentor.bean.dto.ProfesionDto;
import com.example.techmentor.bean.entity.Profesion;
import com.example.techmentor.bean.mapper.ProfesionMapper;
import com.example.techmentor.persistence.ProfesionRepository;
import com.example.techmentor.usecase.ProfesionUseCase;

/** Lógica de negocio de profesión: implementa ProfesionUseCase usando repositorio y mapper. */
@Service
@RequiredArgsConstructor
public class ProfesionModel implements ProfesionUseCase {

    private final ProfesionRepository profesionRepository;
    private final ProfesionMapper profesionMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ProfesionDto> listarActivos() {
        return profesionRepository.findByActivoTrue().stream().map(profesionMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProfesionDto> listarTodos() {
        return profesionRepository.findAll().stream().map(profesionMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProfesionDto obtener(Long id) {
        return profesionMapper.toDto(buscar(id));
    }

    @Override
    @Transactional
    public ProfesionDto crear(ProfesionDto dto) {
        Profesion entity = profesionMapper.toEntity(dto);
        return profesionMapper.toDto(profesionRepository.saveAndFlush(entity));
    }

    @Override
    @Transactional
    public ProfesionDto actualizar(Long id, ProfesionDto dto) {
        Profesion entity = buscar(id);
        profesionMapper.actualizar(dto, entity);
        return profesionMapper.toDto(profesionRepository.saveAndFlush(entity));
    }

    @Override
    @Transactional
    public ProfesionDto cambiarActivo(Long id, boolean valor) {
        Profesion entity = buscar(id);
        entity.setActivo(valor);
        return profesionMapper.toDto(profesionRepository.saveAndFlush(entity));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        profesionRepository.delete(buscar(id));
        profesionRepository.flush();
    }

    private Profesion buscar(Long id) {
        return profesionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No existe profesión con id: " + id));
    }

}
