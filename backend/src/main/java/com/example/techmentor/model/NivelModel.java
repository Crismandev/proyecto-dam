package com.example.techmentor.model;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.example.techmentor.bean.dto.NivelDto;
import com.example.techmentor.bean.entity.Nivel;
import com.example.techmentor.bean.entity.Curso;
import com.example.techmentor.bean.mapper.NivelMapper;
import com.example.techmentor.persistence.NivelRepository;
import com.example.techmentor.persistence.CursoRepository;
import com.example.techmentor.usecase.NivelUseCase;

/** Lógica de negocio de nivel: implementa NivelUseCase usando repositorio y mapper. */
@Service
@RequiredArgsConstructor
public class NivelModel implements NivelUseCase {

    private final NivelRepository nivelRepository;
    private final NivelMapper nivelMapper;
    private final CursoRepository cursoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<NivelDto> listar() {
        return nivelRepository.findAll().stream().map(nivelMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<NivelDto> listarPorCurso(Long idCurso) {
        return nivelRepository.findByCurso_IdCursoOrderByOrdenAsc(idCurso).stream().map(nivelMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public NivelDto obtener(Long id) {
        return nivelMapper.toDto(buscar(id));
    }

    @Override
    @Transactional
    public NivelDto crear(NivelDto dto) {
        Nivel entity = nivelMapper.toEntity(dto);
        entity.setCurso(resolverCurso(dto.getIdCurso()));
        return nivelMapper.toDto(nivelRepository.saveAndFlush(entity));
    }

    @Override
    @Transactional
    public NivelDto actualizar(Long id, NivelDto dto) {
        Nivel entity = buscar(id);
        nivelMapper.actualizar(dto, entity);
        entity.setCurso(resolverCurso(dto.getIdCurso()));
        return nivelMapper.toDto(nivelRepository.saveAndFlush(entity));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        nivelRepository.delete(buscar(id));
        nivelRepository.flush();
    }

    private Nivel buscar(Long id) {
        return nivelRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No existe nivel con id: " + id));
    }

    private Curso resolverCurso(Long id) {
        return cursoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No existe curso con id: " + id));
    }

}
