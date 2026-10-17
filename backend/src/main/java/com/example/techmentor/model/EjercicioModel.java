package com.example.techmentor.model;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.example.techmentor.bean.dto.EjercicioDto;
import com.example.techmentor.bean.entity.Ejercicio;
import com.example.techmentor.bean.entity.Leccion;
import com.example.techmentor.bean.mapper.EjercicioMapper;
import com.example.techmentor.persistence.EjercicioRepository;
import com.example.techmentor.persistence.LeccionRepository;
import com.example.techmentor.usecase.EjercicioUseCase;

/** Lógica de negocio de ejercicio: implementa EjercicioUseCase usando repositorio y mapper. */
@Service
@RequiredArgsConstructor
public class EjercicioModel implements EjercicioUseCase {

    private final EjercicioRepository ejercicioRepository;
    private final EjercicioMapper ejercicioMapper;
    private final LeccionRepository leccionRepository;

    @Override
    @Transactional(readOnly = true)
    public List<EjercicioDto> listar() {
        return ejercicioRepository.findAll().stream().map(ejercicioMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EjercicioDto> listarPorLeccion(Long idLeccion) {
        return ejercicioRepository.findByLeccion_IdLeccionOrderByOrdenAsc(idLeccion).stream().map(ejercicioMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public EjercicioDto obtener(Long id) {
        return ejercicioMapper.toDto(buscar(id));
    }

    @Override
    @Transactional
    public EjercicioDto crear(EjercicioDto dto) {
        Ejercicio entity = ejercicioMapper.toEntity(dto);
        entity.setLeccion(resolverLeccion(dto.getIdLeccion()));
        return ejercicioMapper.toDto(ejercicioRepository.saveAndFlush(entity));
    }

    @Override
    @Transactional
    public EjercicioDto actualizar(Long id, EjercicioDto dto) {
        Ejercicio entity = buscar(id);
        ejercicioMapper.actualizar(dto, entity);
        entity.setLeccion(resolverLeccion(dto.getIdLeccion()));
        return ejercicioMapper.toDto(ejercicioRepository.saveAndFlush(entity));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        ejercicioRepository.delete(buscar(id));
        ejercicioRepository.flush();
    }

    private Ejercicio buscar(Long id) {
        return ejercicioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No existe ejercicio con id: " + id));
    }

    private Leccion resolverLeccion(Long id) {
        return leccionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No existe leccion con id: " + id));
    }

}
