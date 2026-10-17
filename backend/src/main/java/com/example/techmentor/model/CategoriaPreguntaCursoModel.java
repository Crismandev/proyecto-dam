package com.example.techmentor.model;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.example.techmentor.bean.dto.CategoriaPreguntaCursoDto;
import com.example.techmentor.bean.entity.CategoriaPreguntaCurso;
import com.example.techmentor.bean.entity.CategoriaPregunta;
import com.example.techmentor.bean.entity.Curso;
import com.example.techmentor.bean.mapper.CategoriaPreguntaCursoMapper;
import com.example.techmentor.persistence.CategoriaPreguntaCursoRepository;
import com.example.techmentor.persistence.CategoriaPreguntaRepository;
import com.example.techmentor.persistence.CursoRepository;
import com.example.techmentor.usecase.CategoriaPreguntaCursoUseCase;

/** Lógica de negocio de relación categoría de pregunta-curso: implementa CategoriaPreguntaCursoUseCase usando repositorio y mapper. */
@Service
@RequiredArgsConstructor
public class CategoriaPreguntaCursoModel implements CategoriaPreguntaCursoUseCase {

    private final CategoriaPreguntaCursoRepository categoriaPreguntaCursoRepository;
    private final CategoriaPreguntaCursoMapper categoriaPreguntaCursoMapper;
    private final CategoriaPreguntaRepository categoriaPreguntaRepository;
    private final CursoRepository cursoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaPreguntaCursoDto> listar() {
        return categoriaPreguntaCursoRepository.findAll().stream().map(categoriaPreguntaCursoMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaPreguntaCursoDto> listarPorCategoriaPregunta(Long idCategoriaPregunta) {
        return categoriaPreguntaCursoRepository.findByCategoriaPregunta_IdCategoriaPregunta(idCategoriaPregunta).stream().map(categoriaPreguntaCursoMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CategoriaPreguntaCursoDto obtener(Long id) {
        return categoriaPreguntaCursoMapper.toDto(buscar(id));
    }

    @Override
    @Transactional
    public CategoriaPreguntaCursoDto crear(CategoriaPreguntaCursoDto dto) {
        CategoriaPreguntaCurso entity = categoriaPreguntaCursoMapper.toEntity(dto);
        entity.setCategoriaPregunta(resolverCategoriaPregunta(dto.getIdCategoriaPregunta()));
        entity.setCurso(resolverCurso(dto.getIdCurso()));
        return categoriaPreguntaCursoMapper.toDto(categoriaPreguntaCursoRepository.saveAndFlush(entity));
    }

    @Override
    @Transactional
    public CategoriaPreguntaCursoDto actualizar(Long id, CategoriaPreguntaCursoDto dto) {
        CategoriaPreguntaCurso entity = buscar(id);
        categoriaPreguntaCursoMapper.actualizar(dto, entity);
        entity.setCategoriaPregunta(resolverCategoriaPregunta(dto.getIdCategoriaPregunta()));
        entity.setCurso(resolverCurso(dto.getIdCurso()));
        return categoriaPreguntaCursoMapper.toDto(categoriaPreguntaCursoRepository.saveAndFlush(entity));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        categoriaPreguntaCursoRepository.delete(buscar(id));
        categoriaPreguntaCursoRepository.flush();
    }

    private CategoriaPreguntaCurso buscar(Long id) {
        return categoriaPreguntaCursoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No existe relación categoría de pregunta-curso con id: " + id));
    }

    private CategoriaPregunta resolverCategoriaPregunta(Long id) {
        return categoriaPreguntaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No existe categoriaPregunta con id: " + id));
    }

    private Curso resolverCurso(Long id) {
        return cursoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No existe curso con id: " + id));
    }

}
