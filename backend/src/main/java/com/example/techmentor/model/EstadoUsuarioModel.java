package com.example.techmentor.model;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.example.techmentor.bean.dto.EstadoUsuarioDto;
import com.example.techmentor.bean.entity.EstadoUsuario;
import com.example.techmentor.bean.mapper.EstadoUsuarioMapper;
import com.example.techmentor.persistence.EstadoUsuarioRepository;
import com.example.techmentor.usecase.EstadoUsuarioUseCase;

/** Lógica de negocio de estado de usuario: implementa EstadoUsuarioUseCase usando repositorio y mapper. */
@Service
@RequiredArgsConstructor
public class EstadoUsuarioModel implements EstadoUsuarioUseCase {

    private final EstadoUsuarioRepository estadoUsuarioRepository;
    private final EstadoUsuarioMapper estadoUsuarioMapper;

    @Override
    @Transactional(readOnly = true)
    public List<EstadoUsuarioDto> listar() {
        return estadoUsuarioRepository.findAll().stream().map(estadoUsuarioMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public EstadoUsuarioDto obtener(Long id) {
        return estadoUsuarioMapper.toDto(buscar(id));
    }

    @Override
    @Transactional
    public EstadoUsuarioDto crear(EstadoUsuarioDto dto) {
        EstadoUsuario entity = estadoUsuarioMapper.toEntity(dto);
        return estadoUsuarioMapper.toDto(estadoUsuarioRepository.saveAndFlush(entity));
    }

    @Override
    @Transactional
    public EstadoUsuarioDto actualizar(Long id, EstadoUsuarioDto dto) {
        EstadoUsuario entity = buscar(id);
        estadoUsuarioMapper.actualizar(dto, entity);
        return estadoUsuarioMapper.toDto(estadoUsuarioRepository.saveAndFlush(entity));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        estadoUsuarioRepository.delete(buscar(id));
        estadoUsuarioRepository.flush();
    }

    private EstadoUsuario buscar(Long id) {
        return estadoUsuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No existe estado de usuario con id: " + id));
    }

}
