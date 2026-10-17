package com.example.techmentor.model;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.example.techmentor.bean.dto.OpcionEjercicioDto;
import com.example.techmentor.bean.entity.OpcionEjercicio;
import com.example.techmentor.bean.entity.Ejercicio;
import com.example.techmentor.bean.mapper.OpcionEjercicioMapper;
import com.example.techmentor.persistence.OpcionEjercicioRepository;
import com.example.techmentor.persistence.EjercicioRepository;
import com.example.techmentor.usecase.OpcionEjercicioUseCase;

/** Lógica de negocio de opción de ejercicio: implementa OpcionEjercicioUseCase usando repositorio y mapper. */
@Service
@RequiredArgsConstructor
public class OpcionEjercicioModel implements OpcionEjercicioUseCase {

    private final OpcionEjercicioRepository opcionEjercicioRepository;
    private final OpcionEjercicioMapper opcionEjercicioMapper;
    private final EjercicioRepository ejercicioRepository;

    @Override
    @Transactional(readOnly = true)
    public List<OpcionEjercicioDto> listar() {
        return opcionEjercicioRepository.findAll().stream().map(opcionEjercicioMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OpcionEjercicioDto> listarPorEjercicio(Long idEjercicio) {
        return opcionEjercicioRepository.findByEjercicio_IdEjercicioOrderByOrdenAsc(idEjercicio).stream().map(opcionEjercicioMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public OpcionEjercicioDto obtener(Long id) {
        return opcionEjercicioMapper.toDto(buscar(id));
    }

    @Override
    @Transactional
    public OpcionEjercicioDto crear(OpcionEjercicioDto dto) {
        OpcionEjercicio entity = opcionEjercicioMapper.toEntity(dto);
        entity.setEjercicio(resolverEjercicio(dto.getIdEjercicio()));
        return opcionEjercicioMapper.toDto(opcionEjercicioRepository.saveAndFlush(entity));
    }

    @Override
    @Transactional
    public OpcionEjercicioDto actualizar(Long id, OpcionEjercicioDto dto) {
        OpcionEjercicio entity = buscar(id);
        opcionEjercicioMapper.actualizar(dto, entity);
        entity.setEjercicio(resolverEjercicio(dto.getIdEjercicio()));
        return opcionEjercicioMapper.toDto(opcionEjercicioRepository.saveAndFlush(entity));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        opcionEjercicioRepository.delete(buscar(id));
        opcionEjercicioRepository.flush();
    }

    private OpcionEjercicio buscar(Long id) {
        return opcionEjercicioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No existe opción de ejercicio con id: " + id));
    }

    private Ejercicio resolverEjercicio(Long id) {
        return ejercicioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No existe ejercicio con id: " + id));
    }

}
