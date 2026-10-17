package com.example.techmentor.model;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.example.techmentor.bean.dto.RecompensaDto;
import com.example.techmentor.bean.entity.Recompensa;
import com.example.techmentor.bean.mapper.RecompensaMapper;
import com.example.techmentor.persistence.RecompensaRepository;
import com.example.techmentor.usecase.RecompensaUseCase;

/** Lógica de negocio de recompensa: implementa RecompensaUseCase usando repositorio y mapper. */
@Service
@RequiredArgsConstructor
public class RecompensaModel implements RecompensaUseCase {

    private final RecompensaRepository recompensaRepository;
    private final RecompensaMapper recompensaMapper;

    @Override
    @Transactional(readOnly = true)
    public List<RecompensaDto> listarActivos() {
        return recompensaRepository.findByActivaTrue().stream().map(recompensaMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecompensaDto> listarTodos() {
        return recompensaRepository.findAll().stream().map(recompensaMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public RecompensaDto obtener(Long id) {
        return recompensaMapper.toDto(buscar(id));
    }

    @Override
    @Transactional
    public RecompensaDto crear(RecompensaDto dto) {
        Recompensa entity = recompensaMapper.toEntity(dto);
        return recompensaMapper.toDto(recompensaRepository.saveAndFlush(entity));
    }

    @Override
    @Transactional
    public RecompensaDto actualizar(Long id, RecompensaDto dto) {
        Recompensa entity = buscar(id);
        recompensaMapper.actualizar(dto, entity);
        return recompensaMapper.toDto(recompensaRepository.saveAndFlush(entity));
    }

    @Override
    @Transactional
    public RecompensaDto cambiarActivo(Long id, boolean valor) {
        Recompensa entity = buscar(id);
        entity.setActiva(valor);
        return recompensaMapper.toDto(recompensaRepository.saveAndFlush(entity));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        recompensaRepository.delete(buscar(id));
        recompensaRepository.flush();
    }

    private Recompensa buscar(Long id) {
        return recompensaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No existe recompensa con id: " + id));
    }

}
