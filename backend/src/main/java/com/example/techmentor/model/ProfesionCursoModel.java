package com.example.techmentor.model;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.example.techmentor.bean.dto.ProfesionCursoDto;
import com.example.techmentor.bean.entity.ProfesionCurso;
import com.example.techmentor.bean.entity.Profesion;
import com.example.techmentor.bean.entity.Curso;
import com.example.techmentor.bean.mapper.ProfesionCursoMapper;
import com.example.techmentor.persistence.ProfesionCursoRepository;
import com.example.techmentor.persistence.ProfesionRepository;
import com.example.techmentor.persistence.CursoRepository;
import com.example.techmentor.usecase.ProfesionCursoUseCase;

/** Lógica de negocio de relación puesto-curso: implementa ProfesionCursoUseCase usando repositorio y mapper. */
@Service
@RequiredArgsConstructor
public class ProfesionCursoModel implements ProfesionCursoUseCase {

    private final ProfesionCursoRepository profesionCursoRepository;
    private final ProfesionCursoMapper profesionCursoMapper;
    private final ProfesionRepository profesionRepository;
    private final CursoRepository cursoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ProfesionCursoDto> listar() {
        return profesionCursoRepository.findAll().stream().map(profesionCursoMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProfesionCursoDto> listarPorProfesion(Long idProfesion) {
        return profesionCursoRepository.findByProfesion_IdProfesion(idProfesion).stream().map(profesionCursoMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProfesionCursoDto obtener(Long id) {
        return profesionCursoMapper.toDto(buscar(id));
    }

    @Override
    @Transactional
    public ProfesionCursoDto crear(ProfesionCursoDto dto) {
        ProfesionCurso entity = profesionCursoMapper.toEntity(dto);
        entity.setProfesion(resolverProfesion(dto.getIdProfesion()));
        entity.setCurso(resolverCurso(dto.getIdCurso()));
        return profesionCursoMapper.toDto(profesionCursoRepository.saveAndFlush(entity));
    }

    @Override
    @Transactional
    public ProfesionCursoDto actualizar(Long id, ProfesionCursoDto dto) {
        ProfesionCurso entity = buscar(id);
        profesionCursoMapper.actualizar(dto, entity);
        entity.setProfesion(resolverProfesion(dto.getIdProfesion()));
        entity.setCurso(resolverCurso(dto.getIdCurso()));
        return profesionCursoMapper.toDto(profesionCursoRepository.saveAndFlush(entity));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        profesionCursoRepository.delete(buscar(id));
        profesionCursoRepository.flush();
    }

    private ProfesionCurso buscar(Long id) {
        return profesionCursoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No existe relación puesto-curso con id: " + id));
    }

    private Profesion resolverProfesion(Long id) {
        return profesionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No existe profesion con id: " + id));
    }

    private Curso resolverCurso(Long id) {
        return cursoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No existe curso con id: " + id));
    }

}
