package com.techmentor.service;

import com.techmentor.dto.CursoDTO;
import com.techmentor.dto.CursoSaveDTO;
import com.techmentor.entity.Categoria;
import com.techmentor.entity.Curso;
import com.techmentor.exception.ResourceNotFoundException;
import com.techmentor.mapper.CursoMapper;
import com.techmentor.repository.CategoriaRepository;
import com.techmentor.repository.CursoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CursoService {

    private final CursoRepository cursoRepository;
    private final CategoriaRepository categoriaRepository;
    private final CursoMapper cursoMapper;

    @Transactional(readOnly = true)
    public Page<CursoDTO> listar(Long idCategoria, String q, Pageable pageable) {
        String query = q != null ? q.trim() : "";
        return cursoRepository.buscar(idCategoria, query, pageable).map(cursoMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public CursoDTO obtenerPorId(Long id) {
        Curso entity = cursoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Curso no encontrado con ID: " + id));
        return cursoMapper.toDTO(entity);
    }

    @Transactional
    public CursoDTO crear(CursoSaveDTO dto) {
        Categoria categoria = categoriaRepository.findById(dto.getIdCategoria())
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con ID: " + dto.getIdCategoria()));

        Curso entity = new Curso();
        entity.setCategoria(categoria);
        entity.setNombre(dto.getNombreCurso());
        entity.setDescripcion(dto.getDescripcion());
        entity.setIcono(dto.getIcono());
        entity.setXpRequerido(dto.getXpRequerido() != null ? dto.getXpRequerido() : 0);
        entity.setXpRecompensa(dto.getXpRecompensa() != null ? dto.getXpRecompensa() : 0);

        Curso guardado = cursoRepository.save(entity);
        return cursoMapper.toDTO(guardado);
    }

    @Transactional
    public CursoDTO actualizar(Long id, CursoSaveDTO dto) {
        Curso entity = cursoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Curso no encontrado con ID: " + id));

        Categoria categoria = categoriaRepository.findById(dto.getIdCategoria())
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con ID: " + dto.getIdCategoria()));

        entity.setCategoria(categoria);
        entity.setNombre(dto.getNombreCurso());
        entity.setDescripcion(dto.getDescripcion());
        entity.setIcono(dto.getIcono());
        entity.setXpRequerido(dto.getXpRequerido() != null ? dto.getXpRequerido() : 0);
        entity.setXpRecompensa(dto.getXpRecompensa() != null ? dto.getXpRecompensa() : 0);

        Curso actualizado = cursoRepository.save(entity);
        return cursoMapper.toDTO(actualizado);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!cursoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Curso no encontrado con ID: " + id);
        }
        cursoRepository.deleteById(id);
    }
}
