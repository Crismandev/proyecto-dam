package com.example.techmentor.model;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.example.techmentor.bean.dto.RolDto;
import com.example.techmentor.bean.entity.Rol;
import com.example.techmentor.bean.mapper.RolMapper;
import com.example.techmentor.persistence.RolRepository;
import com.example.techmentor.usecase.RolUseCase;

/** Lógica de negocio de rol: implementa RolUseCase usando repositorio y mapper. */
@Service
@RequiredArgsConstructor
public class RolModel implements RolUseCase {

    private final RolRepository rolRepository;
    private final RolMapper rolMapper;

    @Override
    @Transactional(readOnly = true)
    public List<RolDto> listar() {
        return rolRepository.findAll().stream().map(rolMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public RolDto obtener(Long id) {
        return rolMapper.toDto(buscar(id));
    }

    @Override
    @Transactional
    public RolDto crear(RolDto dto) {
        Rol entity = rolMapper.toEntity(dto);
        return rolMapper.toDto(rolRepository.saveAndFlush(entity));
    }

    @Override
    @Transactional
    public RolDto actualizar(Long id, RolDto dto) {
        Rol entity = buscar(id);
        rolMapper.actualizar(dto, entity);
        return rolMapper.toDto(rolRepository.saveAndFlush(entity));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        rolRepository.delete(buscar(id));
        rolRepository.flush();
    }

    private Rol buscar(Long id) {
        return rolRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No existe rol con id: " + id));
    }

}
