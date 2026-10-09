package com.techmentor.service;

import com.techmentor.dto.CategoriaDTO;
import com.techmentor.entity.Categoria;
import com.techmentor.exception.ConflictException;
import com.techmentor.exception.ResourceNotFoundException;
import com.techmentor.mapper.CategoriaMapper;
import com.techmentor.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper categoriaMapper;

    @Transactional(readOnly = true)
    public Page<CategoriaDTO> listar(String q, Pageable pageable) {
        String query = q != null ? q.trim() : "";
        return categoriaRepository.buscar(query, pageable).map(categoriaMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public CategoriaDTO obtenerPorId(Long id) {
        Categoria entity = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con ID: " + id));
        return categoriaMapper.toDTO(entity);
    }

    @Transactional
    public CategoriaDTO crear(CategoriaDTO dto) {
        if (categoriaRepository.existsByNombre(dto.getNombreCategoria())) {
            throw new ConflictException("La categoría '" + dto.getNombreCategoria() + "' ya existe");
        }
        Categoria entity = categoriaMapper.toEntity(dto);
        entity.setIdCategoria(null);
        Categoria guardada = categoriaRepository.save(entity);
        return categoriaMapper.toDTO(guardada);
    }

    @Transactional
    public CategoriaDTO actualizar(Long id, CategoriaDTO dto) {
        Categoria entity = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con ID: " + id));

        if (!entity.getNombre().equalsIgnoreCase(dto.getNombreCategoria()) && categoriaRepository.existsByNombre(dto.getNombreCategoria())) {
            throw new ConflictException("La categoría '" + dto.getNombreCategoria() + "' ya existe");
        }

        entity.setNombre(dto.getNombreCategoria());
        entity.setDescripcion(dto.getDescripcion());
        entity.setIcono(dto.getIcono());
        Categoria actualizada = categoriaRepository.save(entity);
        return categoriaMapper.toDTO(actualizada);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!categoriaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Categoría no encontrada con ID: " + id);
        }
        categoriaRepository.deleteById(id);
    }
}
