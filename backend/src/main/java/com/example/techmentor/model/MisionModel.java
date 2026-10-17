package com.example.techmentor.model;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.example.techmentor.bean.dto.MisionDto;
import com.example.techmentor.bean.entity.Mision;
import com.example.techmentor.bean.mapper.MisionMapper;
import com.example.techmentor.persistence.MisionRepository;
import com.example.techmentor.usecase.MisionUseCase;

/** Lógica de negocio de misión: implementa MisionUseCase usando repositorio y mapper. */
@Service
@RequiredArgsConstructor
public class MisionModel implements MisionUseCase {

    private final MisionRepository misionRepository;
    private final MisionMapper misionMapper;

    @Override
    @Transactional(readOnly = true)
    public List<MisionDto> listarActivos() {
        return misionRepository.findByActivaTrue().stream().map(misionMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MisionDto> listarTodos() {
        return misionRepository.findAll().stream().map(misionMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public MisionDto obtener(Long id) {
        return misionMapper.toDto(buscar(id));
    }

    @Override
    @Transactional
    public MisionDto crear(MisionDto dto) {
        Mision entity = misionMapper.toEntity(dto);
        return misionMapper.toDto(misionRepository.saveAndFlush(entity));
    }

    @Override
    @Transactional
    public MisionDto actualizar(Long id, MisionDto dto) {
        Mision entity = buscar(id);
        misionMapper.actualizar(dto, entity);
        return misionMapper.toDto(misionRepository.saveAndFlush(entity));
    }

    @Override
    @Transactional
    public MisionDto cambiarActivo(Long id, boolean valor) {
        Mision entity = buscar(id);
        entity.setActiva(valor);
        return misionMapper.toDto(misionRepository.saveAndFlush(entity));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        misionRepository.delete(buscar(id));
        misionRepository.flush();
    }

    private Mision buscar(Long id) {
        return misionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No existe misión con id: " + id));
    }

}
