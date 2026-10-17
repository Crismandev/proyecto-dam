package com.example.techmentor.model;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.example.techmentor.bean.dto.CursoDto;
import com.example.techmentor.bean.entity.Curso;
import com.example.techmentor.bean.entity.Categoria;
import com.example.techmentor.bean.mapper.CursoMapper;
import com.example.techmentor.persistence.CursoRepository;
import com.example.techmentor.persistence.CategoriaRepository;
import com.example.techmentor.usecase.CursoUseCase;

/** Lógica de negocio de curso: implementa CursoUseCase usando repositorio y mapper. */
@Service
@RequiredArgsConstructor
public class CursoModel implements CursoUseCase {

    private final CursoRepository cursoRepository;
    private final CursoMapper cursoMapper;
    private final CategoriaRepository categoriaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CursoDto> listarActivos(Long idCategoria) {
        List<Curso> lista = (idCategoria == null)
                ? cursoRepository.findByActivoTrue()
                : cursoRepository.findByActivoTrueAndCategoria_IdCategoria(idCategoria);
        return lista.stream().map(cursoMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CursoDto> listarTodos() {
        return cursoRepository.findAll().stream().map(cursoMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CursoDto obtener(Long id) {
        return cursoMapper.toDto(buscar(id));
    }

    @Override
    @Transactional
    public CursoDto crear(CursoDto dto) {
        Curso entity = cursoMapper.toEntity(dto);
        entity.setCategoria(resolverCategoria(dto.getIdCategoria()));
        return cursoMapper.toDto(cursoRepository.saveAndFlush(entity));
    }

    @Override
    @Transactional
    public CursoDto actualizar(Long id, CursoDto dto) {
        Curso entity = buscar(id);
        cursoMapper.actualizar(dto, entity);
        entity.setCategoria(resolverCategoria(dto.getIdCategoria()));
        return cursoMapper.toDto(cursoRepository.saveAndFlush(entity));
    }

    @Override
    @Transactional
    public CursoDto cambiarActivo(Long id, boolean valor) {
        Curso entity = buscar(id);
        entity.setActivo(valor);
        return cursoMapper.toDto(cursoRepository.saveAndFlush(entity));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        cursoRepository.delete(buscar(id));
        cursoRepository.flush();
    }

    private Curso buscar(Long id) {
        return cursoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No existe curso con id: " + id));
    }

    private Categoria resolverCategoria(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No existe categoria con id: " + id));
    }

}
