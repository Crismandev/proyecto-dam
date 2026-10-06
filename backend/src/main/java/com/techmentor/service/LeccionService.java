package com.techmentor.service;

import com.techmentor.dto.LeccionDTO;
import com.techmentor.dto.LeccionSaveDTO;
import com.techmentor.entity.Leccion;
import com.techmentor.entity.Nivel;
import com.techmentor.exception.ResourceNotFoundException;
import com.techmentor.mapper.LeccionMapper;
import com.techmentor.repository.LeccionRepository;
import com.techmentor.repository.NivelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LeccionService {

    private final LeccionRepository leccionRepository;
    private final NivelRepository nivelRepository;
    private final LeccionMapper leccionMapper;

    @Transactional(readOnly = true)
    public Page<LeccionDTO> listar(Long idNivel, String q, Pageable pageable) {
        String query = q != null ? q.trim() : "";
        return leccionRepository.buscar(idNivel, query, pageable).map(leccionMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public LeccionDTO obtenerPorId(Long id) {
        Leccion entity = leccionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lección no encontrada con ID: " + id));
        return leccionMapper.toDTO(entity);
    }

    @Transactional
    public LeccionDTO crear(LeccionSaveDTO dto) {
        Nivel nivel = nivelRepository.findById(dto.getIdNivel())
                .orElseThrow(() -> new ResourceNotFoundException("Nivel no encontrado con ID: " + dto.getIdNivel()));

        Leccion entity = new Leccion();
        entity.setNivel(nivel);
        entity.setTitulo(dto.getTitulo());
        entity.setOrden(dto.getOrden());
        entity.setDescripcion(dto.getDescripcion());

        Leccion guardada = leccionRepository.save(entity);
        return leccionMapper.toDTO(guardada);
    }

    @Transactional
    public LeccionDTO actualizar(Long id, LeccionSaveDTO dto) {
        Leccion entity = leccionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lección no encontrada con ID: " + id));

        Nivel nivel = nivelRepository.findById(dto.getIdNivel())
                .orElseThrow(() -> new ResourceNotFoundException("Nivel no encontrado con ID: " + dto.getIdNivel()));

        entity.setNivel(nivel);
        entity.setTitulo(dto.getTitulo());
        entity.setOrden(dto.getOrden());
        entity.setDescripcion(dto.getDescripcion());

        Leccion actualizada = leccionRepository.save(entity);
        return leccionMapper.toDTO(actualizada);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!leccionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Lección no encontrada con ID: " + id);
        }
        leccionRepository.deleteById(id);
    }
}
