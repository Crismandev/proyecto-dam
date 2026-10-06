package com.techmentor.service;

import com.techmentor.dto.NivelDTO;
import com.techmentor.dto.NivelSaveDTO;
import com.techmentor.entity.Curso;
import com.techmentor.entity.Nivel;
import com.techmentor.exception.ResourceNotFoundException;
import com.techmentor.mapper.NivelMapper;
import com.techmentor.repository.CursoRepository;
import com.techmentor.repository.NivelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NivelService {

    private final NivelRepository nivelRepository;
    private final CursoRepository cursoRepository;
    private final NivelMapper nivelMapper;

    @Transactional(readOnly = true)
    public Page<NivelDTO> listar(Long idCurso, String q, Pageable pageable) {
        String query = q != null ? q.trim() : "";
        return nivelRepository.buscar(idCurso, query, pageable).map(nivelMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public NivelDTO obtenerPorId(Long id) {
        Nivel entity = nivelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Nivel no encontrado con ID: " + id));
        return nivelMapper.toDTO(entity);
    }

    @Transactional
    public NivelDTO crear(NivelSaveDTO dto) {
        Curso curso = cursoRepository.findById(dto.getIdCurso())
                .orElseThrow(() -> new ResourceNotFoundException("Curso no encontrado con ID: " + dto.getIdCurso()));

        Nivel entity = new Nivel();
        entity.setCurso(curso);
        entity.setNombre(dto.getNombreNivel());
        entity.setOrden(dto.getOrden());
        entity.setDescripcion(dto.getDescripcion());

        Nivel guardado = nivelRepository.save(entity);
        return nivelMapper.toDTO(guardado);
    }

    @Transactional
    public NivelDTO actualizar(Long id, NivelSaveDTO dto) {
        Nivel entity = nivelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Nivel no encontrado con ID: " + id));

        Curso curso = cursoRepository.findById(dto.getIdCurso())
                .orElseThrow(() -> new ResourceNotFoundException("Curso no encontrado con ID: " + dto.getIdCurso()));

        entity.setCurso(curso);
        entity.setNombre(dto.getNombreNivel());
        entity.setOrden(dto.getOrden());
        entity.setDescripcion(dto.getDescripcion());

        Nivel actualizado = nivelRepository.save(entity);
        return nivelMapper.toDTO(actualizado);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!nivelRepository.existsById(id)) {
            throw new ResourceNotFoundException("Nivel no encontrado con ID: " + id);
        }
        nivelRepository.deleteById(id);
    }
}
