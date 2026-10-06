package com.techmentor.service;

import com.techmentor.dto.RolDTO;
import com.techmentor.entity.Rol;
import com.techmentor.exception.ResourceNotFoundException;
import com.techmentor.mapper.RolMapper;
import com.techmentor.repository.RolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RolService {

    private final RolRepository rolRepository;
    private final RolMapper rolMapper;

    @Transactional(readOnly = true)
    public Page<RolDTO> listar(String q, Pageable pageable) {
        String query = q != null ? q.trim() : "";
        return rolRepository.buscar(query, pageable).map(rolMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public RolDTO obtenerPorId(Long id) {
        Rol entity = rolRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado con ID: " + id));
        return rolMapper.toDTO(entity);
    }

    @Transactional
    public RolDTO crear(RolDTO dto) {
        Rol entity = rolMapper.toEntity(dto);
        entity.setIdRol(null);
        Rol guardado = rolRepository.save(entity);
        return rolMapper.toDTO(guardado);
    }

    @Transactional
    public RolDTO actualizar(Long id, RolDTO dto) {
        Rol entity = rolRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado con ID: " + id));
        entity.setNombre(dto.getNombreRol());
        Rol actualizado = rolRepository.save(entity);
        return rolMapper.toDTO(actualizado);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!rolRepository.existsById(id)) {
            throw new ResourceNotFoundException("Rol no encontrado con ID: " + id);
        }
        rolRepository.deleteById(id);
    }
}
