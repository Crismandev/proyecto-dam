package com.example.techmentor.model;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.example.techmentor.bean.dto.CategoriaDto;
import com.example.techmentor.bean.entity.Categoria;
import com.example.techmentor.bean.mapper.CategoriaMapper;
import com.example.techmentor.persistence.CategoriaRepository;
import com.example.techmentor.usecase.CategoriaUseCase;

/** Lógica de negocio de categoría: implementa CategoriaUseCase usando repositorio y mapper. */
@Service
@RequiredArgsConstructor
public class CategoriaModel implements CategoriaUseCase {

    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper categoriaMapper;

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaDto> listarActivos() {
        return categoriaRepository.findByActivoTrue().stream().map(categoriaMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaDto> listarTodos() {
        return categoriaRepository.findAll().stream().map(categoriaMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CategoriaDto obtener(Long id) {
        return categoriaMapper.toDto(buscar(id));
    }

    @Override
    @Transactional
    public CategoriaDto crear(CategoriaDto dto) {
        Categoria entity = categoriaMapper.toEntity(dto);
        return categoriaMapper.toDto(categoriaRepository.saveAndFlush(entity));
    }

    @Override
    @Transactional
    public CategoriaDto actualizar(Long id, CategoriaDto dto) {
        Categoria entity = buscar(id);
        categoriaMapper.actualizar(dto, entity);
        return categoriaMapper.toDto(categoriaRepository.saveAndFlush(entity));
    }

    @Override
    @Transactional
    public CategoriaDto cambiarActivo(Long id, boolean valor) {
        Categoria entity = buscar(id);
        entity.setActivo(valor);
        return categoriaMapper.toDto(categoriaRepository.saveAndFlush(entity));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        categoriaRepository.delete(buscar(id));
        categoriaRepository.flush();
    }

    private Categoria buscar(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No existe categoría con id: " + id));
    }

}
